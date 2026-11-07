package me.gblrod.composechatbot.feature.chat.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import me.gblrod.composechatbot.R
import me.gblrod.composechatbot.domain.exception.ChatException
import me.gblrod.composechatbot.domain.model.ChatMessage
import me.gblrod.composechatbot.domain.model.MessageRole
import me.gblrod.composechatbot.domain.model.MessageStatus
import me.gblrod.composechatbot.domain.repository.ChatRepository
import me.gblrod.composechatbot.feature.chat.state.ChatUiEvent
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    val messageList = mutableStateListOf<ChatMessage>()

    var isLoading by mutableStateOf(false)
        private set

    var isHistoryLoaded by mutableStateOf(false)
        private set

    var isClearing by mutableStateOf(false)
        private set

    private val _events = Channel<ChatUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var currentJob: Job? = null

    init {
        viewModelScope.launch {
            try {
                chatRepository.markPendingMessagesAsFailed()
            } catch (e: CancellationException) {
                throw e
            } catch (e: ChatException.Persistence) {
                Log.e("ChatViewModel", "Erro ao recuperar mensagens pendentes", e)

                _events.send(element = ChatUiEvent.ShowSnackbar(message = R.string.chat_exception_persistence))
            }

            chatRepository.messages.collect {
                messageList.clear()
                messageList.addAll(elements = it)

                isHistoryLoaded = true
            }
        }
    }

    fun sendMessage(question: String) {
        val cleanQuestion = question.trim()
        if (!isHistoryLoaded || cleanQuestion.isBlank() || isLoading || isClearing) return
        val history = messageList.filter {
            it.status == MessageStatus.SENT
        }

        val userMessage = ChatMessage(
            message = cleanQuestion,
            role = MessageRole.USER,
            status = MessageStatus.PENDING
        )

        isLoading = true

        currentJob = viewModelScope.launch {
            var userMessageId: Long? = null

            try {
                userMessageId = chatRepository.insertMessage(
                    message = userMessage
                )

                val response = chatRepository.sendMessage(
                    question = cleanQuestion,
                    history = history
                )

                val modelMessage = ChatMessage(
                    message = response,
                    role = MessageRole.MODEL
                )

                chatRepository.completeUserMessage(
                    userMessageId = userMessageId,
                    modelMessage = modelMessage
                )

            } catch (e: CancellationException) {
                throw e

            } catch (e: ChatException) {
                Log.e("ChatViewModel", "Erro ao enviar mensagem", e)

                val error = if (userMessageId != null) {
                    try {
                        chatRepository.updateMessageStatus(
                            messageId = userMessageId,
                            status = MessageStatus.FAILED
                        )

                        e
                    } catch (persistenceError: ChatException.Persistence) {
                        Log.e(
                            "ChatViewModel",
                            "Erro ao atualizar status da mensagem",
                            persistenceError
                        )

                        persistenceError
                    }
                } else e

                val message = when (error) {
                    is ChatException.Network -> R.string.chat_exception_network
                    is ChatException.QuotaExceeded -> R.string.chat_exception_quota_exceeded
                    is ChatException.Timeout -> R.string.chat_exception_timeout
                    is ChatException.ContentBlocked -> R.string.chat_exception_content_blocked
                    is ChatException.Server -> R.string.chat_exception_server
                    is ChatException.Persistence -> R.string.chat_exception_persistence
                    is ChatException.Unknown -> R.string.chat_exception_unknown
                }

                _events.send(element = ChatUiEvent.ShowSnackbar(message))

            } finally {
                isLoading = false
                currentJob = null
            }
        }
    }

    fun clearConversation() {
        if (isClearing) return

        isClearing = true

        viewModelScope.launch {
            try {
                currentJob?.cancelAndJoin()
                currentJob = null

                chatRepository.clearMessages()

            } catch (e: CancellationException) {
                throw e

            } catch (e: ChatException.Persistence) {
                Log.e("ChatViewModel", "Erro ao limpar conversa", e)

                _events.send(element = ChatUiEvent.ShowSnackbar(R.string.chat_exception_persistence))

            } finally {
                currentJob = null
                isLoading = false
                isClearing = false
            }
        }
    }
}
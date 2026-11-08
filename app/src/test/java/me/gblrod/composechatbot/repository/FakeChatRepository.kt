package me.gblrod.composechatbot.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import me.gblrod.composechatbot.domain.exception.ChatException
import me.gblrod.composechatbot.domain.model.ChatMessage
import me.gblrod.composechatbot.domain.model.MessageStatus
import me.gblrod.composechatbot.domain.repository.ChatRepository

class FakeChatRepository(
    initialMessages: List<ChatMessage> = emptyList(),
    private val recoveryException: ChatException.Persistence? = null
) : ChatRepository {
    private val _messages = MutableStateFlow(initialMessages)

    val operations = mutableListOf<Operation>()

    override val messages: Flow<List<ChatMessage>> = flow {
        operations.add(Operation.COLLECT_MESSAGES)
        emitAll(_messages)
    }

    override suspend fun markPendingMessagesAsFailed(): Int {
        operations.add(Operation.MARK_PENDING_MESSAGES_AS_FAILED)

        recoveryException?.let { throw it }

        val pendingCount = _messages.value.count {
            it.status == MessageStatus.PENDING
        }

        _messages.value = _messages.value.map { message ->
            if (message.status == MessageStatus.PENDING) {
                message.copy(status = MessageStatus.FAILED)
            } else message
        }

        return pendingCount
    }

    fun emitMessages(messages: List<ChatMessage>) {
        _messages.value = messages
    }

    override suspend fun sendMessage(question: String, history: List<ChatMessage>): String {
        throw NotImplementedError()
    }

    override suspend fun insertMessage(message: ChatMessage): Long {
        throw NotImplementedError()
    }

    override suspend fun updateMessageStatus(messageId: Long, status: MessageStatus) {
        throw NotImplementedError()
    }

    override suspend fun completeUserMessage(userMessageId: Long, modelMessage: ChatMessage) {
        throw NotImplementedError()
    }

    override suspend fun clearMessages() {
        throw NotImplementedError()
    }

    enum class Operation {
        MARK_PENDING_MESSAGES_AS_FAILED,
        COLLECT_MESSAGES
    }
}
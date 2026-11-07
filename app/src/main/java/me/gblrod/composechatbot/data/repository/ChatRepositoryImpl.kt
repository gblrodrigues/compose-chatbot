package me.gblrod.composechatbot.data.repository

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.gblrod.composechatbot.data.remote.safeApiCall
import me.gblrod.composechatbot.data.room.dao.ChatMessageDao
import me.gblrod.composechatbot.data.room.mapper.toChatEntity
import me.gblrod.composechatbot.data.room.mapper.toChatMessage
import me.gblrod.composechatbot.domain.exception.ChatException
import me.gblrod.composechatbot.domain.model.ChatMessage
import me.gblrod.composechatbot.domain.model.MessageStatus
import me.gblrod.composechatbot.domain.repository.ChatRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class ChatRepositoryImpl @Inject constructor(
    private val chatMessageDao: ChatMessageDao
) : ChatRepository {
    private val generativeModel = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(modelName = "gemini-3.1-flash-lite")

    override val messages: Flow<List<ChatMessage>> =
        chatMessageDao.getAllMessages().map { entities ->
            entities.map { it.toChatMessage() }
        }

    override suspend fun sendMessage(question: String, history: List<ChatMessage>): String =
        safeApiCall {
            val firebaseHistory = history.map { message ->
                content(role = message.role.value) {
                    text(message.message)
                }
            }

            val chat = generativeModel.startChat(history = firebaseHistory)
            val response = chat.sendMessage(prompt = question)

            response.text ?: throw IllegalStateException("Resposta vazia do Gemini.")
        }

    override suspend fun insertMessage(message: ChatMessage): Long = safeDatabaseCall {
        chatMessageDao.insertMessage(message.toChatEntity())
    }

    override suspend fun updateMessageStatus(messageId: Long, status: MessageStatus) =
        safeDatabaseCall {
            val updatedRows = chatMessageDao.updateMessageStatus(
                messageId = messageId,
                status = status.value
            )

            check(value = updatedRows == 1) {
                "Message not found: $messageId"
            }
        }

    override suspend fun completeUserMessage(userMessageId: Long, modelMessage: ChatMessage) =
        safeDatabaseCall {
            chatMessageDao.completeUserMessage(
                userMessageId = userMessageId,
                modelMessage = modelMessage.toChatEntity()
            )
        }

    override suspend fun markPendingMessagesAsFailed(): Int = safeDatabaseCall {
        chatMessageDao.markPendingMessagesAsFailed()
    }

    override suspend fun clearMessages() = safeDatabaseCall {
        chatMessageDao.deleteAllMessages()
    }
}

private suspend fun <T> safeDatabaseCall(block: suspend () -> T): T {
    return try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        throw ChatException.Persistence(cause = e)
    }
}
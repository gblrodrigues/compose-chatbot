package me.gblrod.composechatbot.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.webkit.MimeTypeMap
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.gblrod.composechatbot.data.remote.safeApiCall
import me.gblrod.composechatbot.data.room.dao.ChatMessageDao
import me.gblrod.composechatbot.data.room.mapper.toChatEntity
import me.gblrod.composechatbot.data.room.mapper.toChatMessage
import me.gblrod.composechatbot.domain.exception.ChatException
import me.gblrod.composechatbot.domain.model.ChatMessage
import me.gblrod.composechatbot.domain.model.MessageStatus
import me.gblrod.composechatbot.domain.repository.ChatRepository
import java.io.File
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class ChatRepositoryImpl @Inject constructor(
    private val chatMessageDao: ChatMessageDao,
    @ApplicationContext private val context: Context
) : ChatRepository {
    private val generativeModel = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(modelName = "gemini-3.1-flash-lite")

    override val messages: Flow<List<ChatMessage>> =
        chatMessageDao.getAllMessages().map { entities ->
            entities.map { it.toChatMessage() }
        }

    override suspend fun saveImage(uri: Uri): String = safeDatabaseCall {
        withContext(Dispatchers.IO) {
            val mimeType = context.contentResolver.getType(uri)

            val extension = MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(mimeType) ?: "jpg"

            val directory = File(context.filesDir, "chat_images")

            if (!directory.exists() && !directory.mkdirs()) {
                throw IOException("Could not create image directory.")
            }

            val file = File(directory, "${UUID.randomUUID()}.$extension")

            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                    ?: throw IOException("Could not open image.")

                inputStream.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                "chat_images/${file.name}"

            } catch (e: CancellationException) {
                file.delete()
                throw e

            } catch (e: Exception) {
                file.delete()
                throw e
            }
        }
    }

    override suspend fun sendMessage(
        question: String,
        history: List<ChatMessage>,
        imageUri: String?
    ): String {
        val bitmap: Bitmap? = imageUri?.let { path ->
            safeDatabaseCall {
                withContext(Dispatchers.IO) {
                    val file = File(context.filesDir, path)

                    if (!file.isFile) {
                        throw IOException("Image file not found: $path")
                    }

                    BitmapFactory.decodeFile(file.absolutePath)
                        ?: throw IOException("Could not decode image: $path")
                }
            }
        }

        return try {
            safeApiCall {
                val firebaseHistory = history.map { message ->
                    content(role = message.role.value) {
                        text(message.message)
                    }
                }

                val userContent = content(role = "user") {
                    bitmap?.let { image(it) }

                    if (question.isNotBlank()) {
                        text(text = question)
                    }
                }

                val chat = generativeModel.startChat(
                    history = firebaseHistory
                )

                val response = chat.sendMessage(
                    prompt = userContent
                )

                response.text ?: throw IllegalStateException("Resposta vazia do Gemini.")
            }
        } finally {
            bitmap?.recycle()
        }
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
package me.gblrod.composechatbot.domain.repository

import kotlinx.coroutines.flow.Flow
import me.gblrod.composechatbot.domain.model.ChatMessage
import me.gblrod.composechatbot.domain.model.MessageStatus

interface ChatRepository {
    val messages: Flow<List<ChatMessage>>
    suspend fun sendMessage(question: String, history: List<ChatMessage>): String
    suspend fun insertMessage(message: ChatMessage): Long
    suspend fun updateMessageStatus(messageId: Long, status: MessageStatus)
    suspend fun completeUserMessage(userMessageId: Long, modelMessage: ChatMessage)
    suspend fun markPendingMessagesAsFailed(): Int
    suspend fun clearMessages()
}
package me.gblrod.composechatbot.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import me.gblrod.composechatbot.data.room.entity.ChatEntity

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC, id ASC")
    fun getAllMessages(): Flow<List<ChatEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatEntity): Long

    @Query("UPDATE chat_messages SET status = :status WHERE id = :messageId")
    suspend fun updateMessageStatus(messageId: Long, status: String): Int

    @Transaction
    suspend fun completeUserMessage(userMessageId: Long, modelMessage: ChatEntity) {
        val updatedRows = updateMessageStatus(
            messageId = userMessageId,
            status = "sent"
        )

        check(updatedRows == 1) {
            "User message not found: $userMessageId"
        }

        insertMessage(modelMessage)
    }

    @Query("UPDATE chat_messages SET status = 'failed' WHERE status = 'pending'")
    suspend fun markPendingMessagesAsFailed(): Int

    @Query("DELETE FROM chat_messages")
    suspend fun deleteAllMessages()
}
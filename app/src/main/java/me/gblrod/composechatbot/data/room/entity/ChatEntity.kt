package me.gblrod.composechatbot.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.gblrod.composechatbot.domain.model.MessageStatus

@Entity(tableName = "chat_messages")
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val message: String,
    val role: String,
    val timestamp: Long = System.currentTimeMillis(),
    @ColumnInfo(defaultValue = "'sent'")
    val status: String = MessageStatus.SENT.value
)
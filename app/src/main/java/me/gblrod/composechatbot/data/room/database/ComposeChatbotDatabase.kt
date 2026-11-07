package me.gblrod.composechatbot.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import me.gblrod.composechatbot.data.room.dao.ChatMessageDao
import me.gblrod.composechatbot.data.room.entity.ChatEntity

@Database(
    entities = [ChatEntity::class],
    version = 2,
    exportSchema = true
)
abstract class ComposeChatbotDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao
}
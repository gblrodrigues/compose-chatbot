package me.gblrod.composechatbot.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.gblrod.composechatbot.data.room.dao.ChatMessageDao
import me.gblrod.composechatbot.data.room.database.ComposeChatbotDatabase
import me.gblrod.composechatbot.data.room.migration.MIGRATION_2_3
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModules {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ComposeChatbotDatabase {
        return Room.databaseBuilder(
            name = "composechatbot.db",
            context = context,
            klass = ComposeChatbotDatabase::class.java
        )
            .addMigrations(MIGRATION_2_3)
            .build()
    }

    @Provides
    fun provideChatMessageDao(
        database: ComposeChatbotDatabase
    ): ChatMessageDao = database.chatMessageDao()
}
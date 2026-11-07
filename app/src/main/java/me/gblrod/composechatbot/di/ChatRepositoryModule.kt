package me.gblrod.composechatbot.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.gblrod.composechatbot.data.repository.ChatRepositoryImpl
import me.gblrod.composechatbot.domain.repository.ChatRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindChatRepository(implementation: ChatRepositoryImpl): ChatRepository
}
package me.gblrod.composechatbot.data.room.mapper

import me.gblrod.composechatbot.data.room.entity.ChatEntity
import me.gblrod.composechatbot.domain.model.ChatMessage
import me.gblrod.composechatbot.domain.model.MessageRole
import me.gblrod.composechatbot.domain.model.MessageStatus

fun ChatEntity.toChatMessage() = ChatMessage(
    id = id,
    message = message,
    role = MessageRole.entries.firstOrNull {
        it.value == role
    } ?: throw IllegalStateException("Unknown message role in database: $role"),
    status = MessageStatus.from(value = status)
)

fun ChatMessage.toChatEntity() = ChatEntity(
    id = id,
    message = message,
    role = role.value,
    status = status.value
)
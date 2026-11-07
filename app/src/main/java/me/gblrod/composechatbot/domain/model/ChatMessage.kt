package me.gblrod.composechatbot.domain.model

data class ChatMessage(
    val message: String,
    val role: MessageRole,
    val id: Long = 0,
    val status: MessageStatus = MessageStatus.SENT
)

enum class MessageRole(
    val value: String
) {
    USER(value = "user"),
    MODEL(value = "model")
}

enum class MessageStatus(val value: String) {
    PENDING(value = "pending"),
    SENT(value = "sent"),
    FAILED(value = "failed");

    companion object {
        fun from(value: String): MessageStatus = entries.firstOrNull { it.value == value } ?: FAILED
    }
}
package me.gblrod.composechatbot.domain.exception

sealed class ChatException(cause: Throwable) : Exception(cause) {
    class Network(cause: Throwable) : ChatException(cause)
    class QuotaExceeded(cause: Throwable) : ChatException(cause)
    class Timeout(cause: Throwable) : ChatException(cause)
    class ContentBlocked(cause: Throwable) : ChatException(cause)
    class Server(cause: Throwable) : ChatException(cause)
    class Persistence(cause: Throwable) : ChatException(cause)
    class Unknown(cause: Throwable) : ChatException(cause)
}
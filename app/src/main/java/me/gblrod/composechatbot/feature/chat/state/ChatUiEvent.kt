package me.gblrod.composechatbot.feature.chat.state

sealed interface ChatUiEvent {
    data class ShowSnackbar(val message: Int) : ChatUiEvent
}
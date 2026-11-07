package me.gblrod.composechatbot.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.gblrod.composechatbot.feature.chat.viewModel.ChatViewModel
import me.gblrod.composechatbot.feature.settings.viewmodel.SettingsViewModel
import me.gblrod.composechatbot.ui.theme.ComposeChatbotTheme

@Composable
fun ComposeChatbotRoot(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    chatViewModel: ChatViewModel = hiltViewModel(),
) {
    val settings by settingsViewModel.uiState.collectAsStateWithLifecycle()

    settings?.let { state ->
        ComposeChatbotTheme(
            themeOptions = state.themeOptions
        ) {
            ComposeChatbotApp(
                messageList = chatViewModel.messageList,
                currentTheme = state.themeOptions,
                onThemeSelected = { settingsViewModel.setTheme(theme = it) },
                onClearConversation = { chatViewModel.clearConversation() },
                onSendMessage = { chatViewModel.sendMessage(question = it) },
                isLoading = chatViewModel.isLoading,
                events = chatViewModel.events,
                isHistoryLoaded = chatViewModel.isHistoryLoaded,
                isClearing = chatViewModel.isClearing
            )
        }
    }
}
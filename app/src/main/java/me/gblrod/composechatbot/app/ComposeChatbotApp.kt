package me.gblrod.composechatbot.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalResources
import kotlinx.coroutines.flow.Flow
import me.gblrod.composechatbot.domain.model.ChatMessage
import me.gblrod.composechatbot.feature.chat.screen.ChatScreen
import me.gblrod.composechatbot.feature.chat.state.ChatUiEvent
import me.gblrod.composechatbot.feature.settings.model.ThemeOptions
import me.gblrod.composechatbot.shared.components.AppSnackbar
import me.gblrod.composechatbot.shared.components.ChatTopBar

@Composable
fun ComposeChatbotApp(
    currentTheme: ThemeOptions,
    onThemeSelected: (ThemeOptions) -> Unit,
    onClearConversation: () -> Unit,
    onSendMessage: (String) -> Unit,
    messageList: List<ChatMessage>,
    isLoading: Boolean,
    events: Flow<ChatUiEvent>,
    isHistoryLoaded: Boolean,
    isClearing: Boolean
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val resources = LocalResources.current
    val configuration = LocalConfiguration.current

    LaunchedEffect(events, configuration) {
        events.collect { event ->
            when (event) {
                is ChatUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = resources.getString(event.message),
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            ChatTopBar(
                currentTheme = currentTheme,
                onThemeSelected = onThemeSelected,
                onClearConversation = { onClearConversation() },
                onShowClearConversationIcon = messageList.isNotEmpty(),
                isClearing = isClearing
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            ) { snackbarData ->
                AppSnackbar(
                    snackbarData = snackbarData
                )
            }
        }
    ) { paddingValues ->
        ChatScreen(
            isLoading = isLoading,
            messageList = messageList,
            onMessageSend = onSendMessage,
            isHistoryLoaded = isHistoryLoaded,
            isClearing = isClearing,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
        )
    }
}
package me.gblrod.composechatbot.feature.chat.screen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import me.gblrod.composechatbot.domain.model.ChatMessage
import me.gblrod.composechatbot.feature.chat.components.ChatInput
import me.gblrod.composechatbot.feature.chat.components.ChatWelcome
import me.gblrod.composechatbot.feature.chat.components.MessageList
import me.gblrod.composechatbot.feature.chat.components.QuickPrompts

@Composable
fun ChatScreen(
    messageList: List<ChatMessage>,
    modifier: Modifier = Modifier,
    onMessageSend: (String) -> Unit,
    isLoading: Boolean,
    isHistoryLoaded: Boolean,
    isClearing: Boolean
) {
    var message by rememberSaveable { mutableStateOf("") }
    var hasStartedConversation by rememberSaveable { mutableStateOf(messageList.isNotEmpty()) }
    val shouldShowWelcome =
        isHistoryLoaded && !hasStartedConversation && message.isEmpty() && messageList.isEmpty()

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(messageList.isEmpty()) {
        if (messageList.isEmpty()) {
            hasStartedConversation = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            }
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (shouldShowWelcome) {
                ChatWelcome()
            }

            MessageList(
                isLoading = isLoading,
                messageList = messageList,
                modifier = Modifier.fillMaxSize()
            )
        }

        if (shouldShowWelcome) {
            QuickPrompts(
                onPromptClick = {
                    hasStartedConversation = true
                    onMessageSend(it)
                    message = ""
                }
            )
        }

        ChatInput(
            message = message,
            onMessageChange = { message = it },
            isLoading = isLoading,
            isHistoryLoaded = isHistoryLoaded,
            isClearing = isClearing,
            onMessageSend = {
                hasStartedConversation = true
                onMessageSend(it)
                message = ""
            }
        )
    }
}
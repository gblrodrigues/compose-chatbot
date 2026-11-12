package me.gblrod.composechatbot.feature.chat.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import me.gblrod.composechatbot.R
import me.gblrod.composechatbot.domain.model.ChatMessage
import me.gblrod.composechatbot.ui.theme.ButtonOnCopyMessageColor
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun CopyMessage(
    chatMessage: ChatMessage,
    onCopyMessage: (String) -> Unit
) {
    var messageCopied by remember(chatMessage.id) { mutableStateOf(false) }

    if (messageCopied) {
        LaunchedEffect(Unit) {
            delay(duration = 2000.milliseconds)
            messageCopied = false
        }
    }

    IconButton(
        onClick = {
            onCopyMessage(chatMessage.message)
            messageCopied = true
        }
    ) {
        Icon(
            imageVector = if (!messageCopied) Icons.Default.ContentCopy else Icons.Default.Check,
            contentDescription = if (!messageCopied) stringResource(id = R.string.copy_message) else stringResource(id = R.string.copy_message_success),
            tint = if (!messageCopied) MaterialTheme.colorScheme.onSurfaceVariant else ButtonOnCopyMessageColor,
            modifier = Modifier.size(20.dp)
        )
    }
}
package me.gblrod.composechatbot.feature.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.gblrod.composechatbot.domain.model.ChatMessage
import me.gblrod.composechatbot.domain.model.MessageRole
import me.gblrod.composechatbot.ui.theme.UserMessageColor

@Composable
fun MessageRow(
    chatMessage: ChatMessage,
    onCopyMessage: (String) -> Unit
) {
    val isModel = chatMessage.role == MessageRole.MODEL

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isModel) Alignment.Start else Alignment.End
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = if (isModel) Dp.Unspecified else 320.dp)
                .padding(vertical = if (isModel) 0.dp else 4.dp)
                .clip(shape = MaterialTheme.shapes.large)
                .background(color = if (isModel) Color.Transparent else UserMessageColor)
                .padding(
                    horizontal = if (isModel) 4.dp else 16.dp,
                    vertical = if (isModel) 2.dp else 12.dp
                )
        ) {
            if (isModel) {
                MarkdownMessage(
                    content = chatMessage.message
                )
            } else {
                Text(
                    text = chatMessage.message,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        if (isModel) {
            CopyMessage(
                chatMessage = chatMessage,
                onCopyMessage = { onCopyMessage(it) }
            )
        } else {
            UserMessageFooter(
                status = chatMessage.status,
                chatMessage = chatMessage,
                onCopyMessage = { onCopyMessage(it) }
            )
        }
    }
}
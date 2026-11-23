package me.gblrod.composechatbot.feature.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
        if (isModel) {
            Box(
                modifier = Modifier.padding(
                    horizontal = 4.dp,
                    vertical = 2.dp
                )
            ) {
                MarkdownMessage(content = chatMessage.message)
            }

            CopyMessage(
                chatMessage = chatMessage,
                onCopyMessage = { onCopyMessage(it) }
            )
        } else {
            Column(
                modifier = Modifier
                    .widthIn(max = 320.dp)
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                chatMessage.imagePath
                    ?.takeIf { it.isNotBlank() }
                    ?.let { imagePath ->
                        ChatMessageImage(imagePath = imagePath)
                    }

                if (chatMessage.message.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .widthIn(max = 320.dp)
                            .clip(shape = MaterialTheme.shapes.large)
                            .background(color = UserMessageColor)
                            .padding(
                                horizontal = 16.dp,
                                vertical = 12.dp
                            )
                    ) {
                        Text(
                            text = chatMessage.message,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            UserMessageFooter(
                status = chatMessage.status,
                chatMessage = chatMessage,
                onCopyMessage = { onCopyMessage(it) }
            )
        }
    }
}
package me.gblrod.composechatbot.feature.chat.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.gblrod.composechatbot.R

@Composable
fun ChatInput(
    message: String,
    onMessageChange: (String) -> Unit,
    isLoading: Boolean,
    onMessageSend: (String) -> Unit,
    isHistoryLoaded: Boolean,
    isClearing: Boolean
) {
    val canSend = message.isNotBlank()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = message,
            onValueChange = { onMessageChange(it) },
            maxLines = 3,
            enabled = isHistoryLoaded && !isClearing,
            shape = MaterialTheme.shapes.large,
            placeholder = {
                Text(
                    text = stringResource(id = R.string.placeholder),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            leadingIcon = {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircleOutline,
                        contentDescription = stringResource(id = R.string.send_file_content_description)
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (canSend && !isLoading) {
                            onMessageSend(message.trim())
                        }
                    },
                    enabled = canSend && !isLoading && isHistoryLoaded && !isClearing
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = stringResource(id = R.string.send_message_content_description)
                    )
                }
            }
        )
    }
}
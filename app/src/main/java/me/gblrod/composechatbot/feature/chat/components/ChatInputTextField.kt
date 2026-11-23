package me.gblrod.composechatbot.feature.chat.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import me.gblrod.composechatbot.R

@Composable
fun ChatInputTextField(
    message: String,
    onMessageChange: (String) -> Unit,
    isLoading: Boolean,
    isHistoryLoaded: Boolean,
    isClearing: Boolean,
    handleSendMessage: () -> Unit,
    canSend: Boolean,
    onAttachClick: () -> Unit,

    ) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = message,
        onValueChange = { onMessageChange(it) },
        maxLines = 3,
        enabled = isHistoryLoaded && !isClearing,
        shape = MaterialTheme.shapes.large,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
        keyboardActions = KeyboardActions(
            onSend = {
                handleSendMessage()
            }
        ),
        placeholder = {
            Text(
                text = stringResource(id = R.string.placeholder),
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        leadingIcon = {
            IconButton(
                onClick = { onAttachClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.send_file_content_description),
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        trailingIcon = {
            IconButton(
                onClick = { handleSendMessage() },
                enabled = canSend && !isLoading && isHistoryLoaded && !isClearing
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    contentDescription = stringResource(id = R.string.send_message_content_description)
                )
            }
        }
    )
}
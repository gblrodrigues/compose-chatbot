package me.gblrod.composechatbot.feature.chat.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp

@Composable
fun ChatInput(
    message: String,
    onMessageChange: (String) -> Unit,
    isLoading: Boolean,
    onMessageSend: (String, Uri?) -> Unit,
    isHistoryLoaded: Boolean,
    isClearing: Boolean
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }
    val canSend = message.isNotBlank() || imageUri != null

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    fun handleSendMessage() {
        if (canSend && !isLoading) {
            onMessageSend(message.trim(), imageUri)
            imageUri = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        if (imageUri != null) {
            ChatInputImagePreview(
                imageUri = imageUri,
                onRemoveClick = { imageUri = null }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChatInputTextField(
                message = message,
                onMessageChange = { onMessageChange(it) },
                isLoading = isLoading,
                isHistoryLoaded = isHistoryLoaded,
                isClearing = isClearing,
                handleSendMessage = { handleSendMessage() },
                canSend = canSend,
                onAttachClick = {
                    focusManager.clearFocus(force = true)
                    keyboardController?.hide()
                    launcher.launch(input = "image/*")
                }
            )
        }
    }
}
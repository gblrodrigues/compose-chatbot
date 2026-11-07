package me.gblrod.composechatbot.feature.chat.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.gblrod.composechatbot.R
import me.gblrod.composechatbot.ui.theme.ButtonContainerDialogColor

@Composable
fun ConfirmClearConversation(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(id = R.string.clear_history_title)
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.clear_history_description)
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = ButtonContainerDialogColor),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = stringResource(id = R.string.confirm_alert_dialog_button),
                    color = MaterialTheme.colorScheme.inverseSurface
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(
                    width = 2.dp,
                    color = ButtonContainerDialogColor
                ),
            ) {
                Text(
                    text = stringResource(id = R.string.cancel_alert_dialog_button),
                    color = ButtonContainerDialogColor
                )
            }
        }
    )
}
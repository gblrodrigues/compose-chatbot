package me.gblrod.composechatbot.feature.chat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.gblrod.composechatbot.R
import me.gblrod.composechatbot.domain.model.MessageStatus
import me.gblrod.composechatbot.ui.theme.FailedMessageColor
import me.gblrod.composechatbot.ui.theme.ModelMessageColor
import me.gblrod.composechatbot.ui.theme.PendingMessageColor
import me.gblrod.composechatbot.ui.theme.SentMessageColor

@Composable
fun MessageStatusIndicator(
    modifier: Modifier = Modifier,
    status: MessageStatus
) {
    Row(
        modifier = modifier
            .padding(
                end = 8.dp,
                bottom = 4.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when (status) {
            MessageStatus.PENDING -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = ModelMessageColor
                )

                Text(
                    text = stringResource(id = R.string.message_status_pending),
                    color = PendingMessageColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            MessageStatus.SENT -> {
                Text(
                    text = stringResource(id = R.string.message_status_sent),
                    color = SentMessageColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            MessageStatus.FAILED -> {
                Text(
                    text = stringResource(id = R.string.message_status_failed),
                    color = FailedMessageColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
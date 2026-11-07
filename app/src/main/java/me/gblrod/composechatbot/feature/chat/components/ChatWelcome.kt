package me.gblrod.composechatbot.feature.chat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.gblrod.composechatbot.R
import me.gblrod.composechatbot.ui.theme.ModelMessageColor

@Composable
fun ChatWelcome(
    modifier: Modifier = Modifier
) {
    val welcomeMessages = listOf(
        stringResource(id = R.string.welcome_message_1),
        stringResource(id = R.string.welcome_message_2),
        stringResource(id = R.string.welcome_message_3),
        stringResource(id = R.string.welcome_message_4),
        stringResource(id = R.string.welcome_message_5)
    )

    val welcomeMessage = rememberSaveable { welcomeMessages.random() }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Message,
            contentDescription = null,
            tint = ModelMessageColor,
            modifier = Modifier.size(48.dp)
        )

        Text(
            text = welcomeMessage,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
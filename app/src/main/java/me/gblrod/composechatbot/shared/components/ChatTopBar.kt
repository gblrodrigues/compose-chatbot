package me.gblrod.composechatbot.shared.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.gblrod.composechatbot.R
import me.gblrod.composechatbot.feature.chat.components.ConfirmClearConversation
import me.gblrod.composechatbot.feature.settings.model.ThemeOptions
import me.gblrod.composechatbot.ui.theme.ButtonClearChatColor
import me.gblrod.composechatbot.ui.theme.resolveDarkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    currentTheme: ThemeOptions,
    onThemeSelected: (ThemeOptions) -> Unit,
    onClearConversation: () -> Unit,
    onShowClearConversationIcon: Boolean,
    isClearing: Boolean
) {
    val isDarkTheme = currentTheme.resolveDarkTheme()
    var onClearConversationDialog by remember { mutableStateOf(false) }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )

                Text(
                    text = stringResource(id = R.string.app_name),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        actions = {
            Box {
                IconButton(
                    onClick = { onThemeSelected(if (isDarkTheme) ThemeOptions.LIGHT else ThemeOptions.DARK) }
                ) {
                    Icon(
                        imageVector = if (isDarkTheme) ThemeOptions.LIGHT.icon else ThemeOptions.DARK.icon,
                        contentDescription = stringResource(id = R.string.change_theme),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Box {
                IconButton(
                    onClick = { onClearConversationDialog = true },
                    enabled = onShowClearConversationIcon && !isClearing
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = stringResource(id = R.string.clear_history_content_description),
                        tint = if (onShowClearConversationIcon) ButtonClearChatColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                }
            }
        }
    )

    if (onClearConversationDialog) {
        ConfirmClearConversation(
            onConfirm = {
                onClearConversation()
                onClearConversationDialog = false
            },
            onDismiss = { onClearConversationDialog = false }
        )
    }
}
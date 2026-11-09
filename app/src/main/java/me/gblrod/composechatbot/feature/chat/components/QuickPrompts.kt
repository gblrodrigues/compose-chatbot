package me.gblrod.composechatbot.feature.chat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun QuickPrompts(
    onPromptClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        QuickPrompt.entries.chunked(size = 2).forEach { prompts ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                prompts.forEach { prompt ->
                    val suggestion = stringResource(id = prompt.suggestion)
                    val message = stringResource(id = prompt.prompt)

                    QuickPromptChip(
                        modifier = Modifier.weight(1f),
                        icon = prompt.icon,
                        iconColor = prompt.iconColor,
                        suggestion = suggestion,
                        onClick = { onPromptClick(message) }
                    )
                }
            }
        }
    }
}
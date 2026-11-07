package me.gblrod.composechatbot.feature.chat.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.model.rememberMarkdownState
import me.gblrod.composechatbot.ui.theme.MarkdownColor

@Composable
fun MarkdownMessage(
    modifier: Modifier = Modifier,
    content: String
) {
    val markdownState = rememberMarkdownState(
        content = content,
        retainState = true
    )

    Markdown(
        markdownState = markdownState,
        modifier = modifier.fillMaxWidth(),
        colors = markdownColor(
            text = MaterialTheme.colorScheme.onSurface,
            codeBackground = MarkdownColor,
            inlineCodeBackground = MarkdownColor
        )
    )
}
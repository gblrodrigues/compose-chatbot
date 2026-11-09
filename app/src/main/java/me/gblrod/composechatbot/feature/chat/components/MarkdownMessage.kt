package me.gblrod.composechatbot.feature.chat.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.compose.extendedspans.ExtendedSpans
import com.mikepenz.markdown.compose.extendedspans.RoundedCornerSpanPainter
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.model.markdownExtendedSpans
import com.mikepenz.markdown.model.rememberMarkdownState
import me.gblrod.composechatbot.ui.theme.MarkdownCodeBackground
import me.gblrod.composechatbot.ui.theme.MarkdownInlineCodeBackground

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
            codeBackground = MarkdownCodeBackground,
            inlineCodeBackground = MarkdownInlineCodeBackground
        ),
        extendedSpans = markdownExtendedSpans {
            remember {
                ExtendedSpans(RoundedCornerSpanPainter(cornerRadius = 5.sp))
            }
        }
    )
}
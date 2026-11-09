package me.gblrod.composechatbot.feature.chat.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import me.gblrod.composechatbot.R
import me.gblrod.composechatbot.ui.theme.CodeIconColor
import me.gblrod.composechatbot.ui.theme.DescriptionIconColor
import me.gblrod.composechatbot.ui.theme.EmailIconColor
import me.gblrod.composechatbot.ui.theme.IdeasIconColor

enum class QuickPrompt(
    val suggestion: Int,
    val prompt: Int,
    val icon: ImageVector,
    val iconColor: Color
) {
    EMAIL(
        suggestion = R.string.quick_prompt_email_suggestion,
        prompt = R.string.quick_prompt_email_prompt,
        icon = Icons.Default.Email,
        iconColor = EmailIconColor
    ),

    SUMMARIZE(
        suggestion = R.string.quick_prompt_summarize_suggestion,
        prompt = R.string.quick_prompt_summarize_prompt,
        icon = Icons.Default.Description,
        iconColor = DescriptionIconColor
    ),

    IDEAS(
        suggestion = R.string.quick_prompt_ideas_suggestion,
        prompt = R.string.quick_prompt_ideas_prompt,
        icon = Icons.Default.Lightbulb,
        iconColor = IdeasIconColor
    ),

    EXPLAIN_CODING(
        suggestion = R.string.quick_prompt_explain_coding_suggestion,
        prompt = R.string.quick_prompt_explain_coding_prompt,
        icon = Icons.Default.Code,
        iconColor = CodeIconColor
    )
}
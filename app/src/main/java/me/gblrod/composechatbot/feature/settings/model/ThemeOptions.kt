package me.gblrod.composechatbot.feature.settings.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.ui.graphics.vector.ImageVector

enum class ThemeOptions(
    val icon: ImageVector
) {
    SYSTEM(icon = Icons.Default.Brightness7),
    LIGHT(icon = Icons.Default.LightMode),
    DARK(icon = Icons.Default.DarkMode)
}
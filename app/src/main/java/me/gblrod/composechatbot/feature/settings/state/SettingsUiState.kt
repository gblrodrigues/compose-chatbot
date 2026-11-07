package me.gblrod.composechatbot.feature.settings.state

import me.gblrod.composechatbot.feature.settings.model.ThemeOptions

data class SettingsUiState(
    val themeOptions: ThemeOptions = ThemeOptions.SYSTEM
)
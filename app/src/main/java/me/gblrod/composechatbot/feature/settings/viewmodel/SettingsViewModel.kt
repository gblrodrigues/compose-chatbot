package me.gblrod.composechatbot.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.gblrod.composechatbot.data.datastore.repository.SettingsRepository
import me.gblrod.composechatbot.feature.settings.model.ThemeOptions
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    val uiState = settingsRepository.settings

    fun setTheme(theme: ThemeOptions) {
        viewModelScope.launch {
            settingsRepository.setTheme(theme = theme)
        }
    }
}
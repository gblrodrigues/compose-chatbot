package me.gblrod.composechatbot.data.datastore.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.gblrod.composechatbot.data.datastore.composechatbotDataStore
import me.gblrod.composechatbot.feature.settings.model.ThemeOptions
import me.gblrod.composechatbot.feature.settings.state.SettingsUiState
import javax.inject.Inject
import javax.inject.Singleton

val themeKey = stringPreferencesKey(name = "theme")

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val repositoryScope = CoroutineScope(
        context = SupervisorJob() + Dispatchers.IO
    )

    val settings: StateFlow<SettingsUiState?> =
        context.composechatbotDataStore.data.map { prefs ->
            SettingsUiState(
                themeOptions = ThemeOptions.entries.firstOrNull {
                    it.name == prefs[themeKey]
                } ?: ThemeOptions.SYSTEM
            )
        }.stateIn(
            scope = repositoryScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    suspend fun setTheme(theme: ThemeOptions) {
        context.composechatbotDataStore.edit { prefs ->
            prefs[themeKey] = theme.name
        }
    }
}
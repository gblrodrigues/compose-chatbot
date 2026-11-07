package me.gblrod.composechatbot.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import me.gblrod.composechatbot.feature.settings.model.ThemeOptions

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color.Black,
    surface = Color(0xFF1C1B1F),
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    onSurface = Color.Black
)

@Composable
fun ComposeChatbotTheme(
    themeOptions: ThemeOptions,
    content: @Composable () -> Unit
) {
    val darkTheme = themeOptions.resolveDarkTheme()
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect

            val controller = WindowCompat.getInsetsController(window, view)

            controller.isAppearanceLightStatusBars = !darkTheme
            controller.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun ThemeOptions.resolveDarkTheme(): Boolean {
    return when (this) {
        ThemeOptions.SYSTEM -> isSystemInDarkTheme()
        ThemeOptions.LIGHT -> false
        ThemeOptions.DARK -> true
    }
}
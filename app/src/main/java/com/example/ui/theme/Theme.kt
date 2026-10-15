package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = IgPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF00376B),
    onPrimaryContainer = Color.White,
    secondary = IgGradientPink,
    onSecondary = Color.White,
    background = IgDarkBackground,
    onBackground = IgDarkTextPrimary,
    surface = IgDarkSurface,
    onSurface = IgDarkTextPrimary,
    surfaceVariant = IgDarkCard,
    onSurfaceVariant = IgDarkTextSecondary,
    outline = IgDarkBorder,
    error = IgRedHeart,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = IgPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F1FF),
    onPrimaryContainer = IgPrimary,
    secondary = IgGradientPink,
    onSecondary = Color.White,
    background = IgLightBackground,
    onBackground = IgLightTextPrimary,
    surface = IgLightBackground,
    onSurface = IgLightTextPrimary,
    surfaceVariant = IgLightSurface,
    onSurfaceVariant = IgLightTextSecondary,
    outline = IgLightBorder,
    error = IgRedHeart,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

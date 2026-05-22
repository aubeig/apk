package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.data.AppAccentColor

// Dynamic palette constructor
fun getDarkColorScheme(accent: AppAccentColor) = darkColorScheme(
    primary = Color(accent.hex),
    onPrimary = Color(0xFF0F0E17),
    primaryContainer = Color(accent.hex).copy(alpha = 0.3f),
    onPrimaryContainer = Color(0xFFF3F4F6),
    secondary = Color(0xFF9CA3AF),
    onSecondary = Color(0xFF1F2937),
    background = Color(0xFF0F0E17), // Premium Cosmic Black/Slate
    onBackground = Color(0xFFF3F4F6),
    surface = Color(0xFF161424), // Elegant violet-tinted deep card surface
    onSurface = Color(0xFFE5E7EB),
    surfaceVariant = Color(0xFF221F38),
    onSurfaceVariant = Color(0xFF9195AB),
    outline = Color(0xFF4B4A65)
)

fun getLightColorScheme(accent: AppAccentColor) = lightColorScheme(
    primary = Color(accent.hex),
    onPrimary = Color.White,
    primaryContainer = Color(accent.hex).copy(alpha = 0.15f),
    onPrimaryContainer = Color(0xFF111827),
    secondary = Color(0xFF4B5563),
    onSecondary = Color.White,
    background = Color(0xFFFAFAFC), // Elegant soft ice light canvas
    onBackground = Color(0xFF111827),
    surface = Color.White,
    onSurface = Color(0xFF1F2937),
    surfaceVariant = Color(0xFFF3F4F6),
    onSurfaceVariant = Color(0xFF4B5563),
    outline = Color(0xFFD1D5DB)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    accentColor: AppAccentColor = AppAccentColor.PURPLE,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) {
        getDarkColorScheme(accentColor)
    } else {
        getLightColorScheme(accentColor)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

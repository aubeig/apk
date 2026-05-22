package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_prefs")

enum class AppThemeMode {
    LIGHT, DARK, SYSTEM
}

enum class AppAccentColor(val hex: Long, val nameLabel: String) {
    PURPLE(0xFF7C3AED, "Royal Violet"),
    TEAL(0xFF0D9488, "Mint Teal"),
    ROSE(0xFFE11D48, "Sleek Rose"),
    AMBER(0xFFD97706, "Solar Amber")
}

class ThemePreferences(private val context: Context) {

    companion object {
        private val THEME_MODE_KEY = intPreferencesKey("theme_mode")
        private val ACCENT_COLOR_KEY = intPreferencesKey("accent_color_index")
    }

    val themeModeFlow: Flow<AppThemeMode> = context.dataStore.data.map { preferences ->
        val modeIndex = preferences[THEME_MODE_KEY] ?: AppThemeMode.SYSTEM.ordinal
        AppThemeMode.entries.getOrElse(modeIndex) { AppThemeMode.SYSTEM }
    }

    val accentColorFlow: Flow<AppAccentColor> = context.dataStore.data.map { preferences ->
        val colorIndex = preferences[ACCENT_COLOR_KEY] ?: AppAccentColor.PURPLE.ordinal
        AppAccentColor.entries.getOrElse(colorIndex) { AppAccentColor.PURPLE }
    }

    suspend fun setThemeMode(mode: AppThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = mode.ordinal
        }
    }

    suspend fun setAccentColor(accent: AppAccentColor) {
        context.dataStore.edit { preferences ->
            preferences[ACCENT_COLOR_KEY] = accent.ordinal
        }
    }
}

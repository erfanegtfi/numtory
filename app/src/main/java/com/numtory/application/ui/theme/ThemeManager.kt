package com.numtory.application.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.numtory.application.data.local.preferences.PreferencesConstants
import com.numtory.application.data.local.preferences.Session

/**
 * App-wide dark/light theme state, backed by [Session] (SharedPreferences).
 *
 * Holds a Compose snapshot [isDarkTheme] so any composable that reads it
 * (e.g. [MyApplicationTheme] in MainActivity, or the toggle in MarketList)
 * recomposes when the theme changes. Registered as a Koin singleton in `dataModule`.
 */
class ThemeManager(private val session: Session) {

    var isDarkTheme by mutableStateOf(
        session.getPreferenceValue(PreferencesConstants.DARK_THEME, false)
    )
        private set

    fun toggle() = setDark(!isDarkTheme)

    fun setDark(dark: Boolean) {
        isDarkTheme = dark
        session.setPreferenceValue(PreferencesConstants.DARK_THEME, dark)
    }
}

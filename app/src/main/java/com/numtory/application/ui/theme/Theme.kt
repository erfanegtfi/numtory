package com.numtory.application.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.eterex.designsystem.theme.darkTypography
import com.eterex.designsystem.theme.lightTypography

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = White,
    secondary = DarkSurface,
    onSecondary = textWhite,
    tertiary = Tertiary,
    onTertiary = textBlack,
    surface = DarkSurface,
    onSurface = textWhite,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = textGrayDark,
    background = DarkBackground,
    onBackground = textWhite,
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = White,
    secondary = Secondary,
    onSecondary = Color.White,
    onTertiary = Color.White,
    tertiary = Tertiary,
    surface = Secondary,
    onSurface = textBlack,
    surfaceVariant = Secondary,
    onSurfaceVariant = textGray,
    background = Color(0xFFF8F8FF),
    onBackground = textBlack,
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = if (darkTheme) darkTypography else lightTypography,
        content = content
    )
}
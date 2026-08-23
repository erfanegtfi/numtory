package com.numtory.application.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.eterex.designsystem.theme.darkTypography
import com.eterex.designsystem.theme.lightTypography

@Immutable
data class AppColors(

    val surface: Color
)

val LightAppColors = AppColors(
    surface = Color(0xFFFFFFFF),
)

val DarkAppColors = AppColors(
    surface = Color(0xFF333333),
)

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
    background = LightBackground,
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
    val appColors = if (darkTheme) {
        DarkAppColors
    } else {
        LightAppColors
    }

    CompositionLocalProvider(
        LocalAppColors provides appColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = if (darkTheme) darkTypography else lightTypography,
            content = content
        )
    }
}

val LocalAppColors = staticCompositionLocalOf {
    LightAppColors
}

val MaterialTheme.appColors: AppColors
    @Composable
    @ReadOnlyComposable
    get() = LocalAppColors.current
package com.numtory.application

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.numtory.application.ui.theme.MyApplicationTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forceRTL()
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ApplyStatusBarTheme()
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    DestinationsNavHost(navGraph = NavGraphs.root)
                }
            }
        }
    }

    private fun forceRTL() {
        // Force RTL layout direction
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL

        resources.configuration.setLayoutDirection(Locale("fa")) // Persian
        // or Locale("ar") for Arabic, Locale("he") for Hebrew
    }
}

@Composable
fun ApplyStatusBarTheme() {
    val view = LocalView.current

    SideEffect {
        val window = (view.context as androidx.activity.ComponentActivity).window
        val insetsController = WindowInsetsControllerCompat(window, view)

        // Set status bar icon colors (light or dark based on background)
        insetsController.isAppearanceLightStatusBars = false
    }
}
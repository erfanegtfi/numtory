package com.numtory.application

import android.os.Bundle
import android.view.Surface
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowInsetsControllerCompat
import com.numtory.application.common.appOpened
import com.numtory.application.features.market.presenter.BottomNavHost
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.numtory.application.ui.theme.MyApplicationTheme
import io.adtrace.sdk.AdTrace
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forceRTL()
        enableEdgeToEdge()
        appOpened()
        setContent {
            MyApplicationTheme {
                ApplyStatusBarTheme()
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        DestinationsNavHost(navGraph = NavGraphs.root)
                    }

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

    override fun onResume() {
        super.onResume()
        AdTrace.onResume()
    }

    override fun onPause() {
        super.onPause()
        AdTrace.onPause()
    }
}

@Composable
fun ApplyStatusBarTheme() {
    val view = LocalView.current

    SideEffect {
        val window = (view.context as ComponentActivity).window
        val insetsController = WindowInsetsControllerCompat(window, view)

        // Set status bar icon colors (light or dark based on background)
        insetsController.isAppearanceLightStatusBars = false
    }
}
package com.numtory.application

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
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

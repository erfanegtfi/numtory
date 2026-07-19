package com.numtory.application

import android.content.Intent
import android.os.Bundle
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
import com.numtory.application.features.chart.AppChartWebView
import com.numtory.application.features.notification.data.DeepLinkRouter
import com.numtory.application.features.notification.data.PushConstants
import com.numtory.application.features.notification.presenter.RequestNotificationPermission
import com.numtory.application.ui.theme.CHART_SCRIPT
import com.numtory.application.ui.theme.MyApplicationTheme
import com.numtory.application.ui.theme.ThemeManager
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import io.adtrace.sdk.AdTrace
import org.koin.android.ext.android.inject
import org.koin.compose.koinInject
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val deepLinkRouter: DeepLinkRouter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forceRTL()
        enableEdgeToEdge()
        appOpened()
        handleDeepLink(intent)
        setContent {
            val themeManager = koinInject<ThemeManager>()
            MyApplicationTheme(darkTheme = themeManager.isDarkTheme) {
                ApplyStatusBarTheme(darkTheme = themeManager.isDarkTheme)
                RequestNotificationPermission()
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

    /**
     * Fires for taps while the activity is already alive — launchMode is singleTop, so a tap
     * reuses this instance rather than delivering the route through onCreate.
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
        deepLinkRouter.push(intent?.getStringExtra(PushConstants.KEY_ROUTE))
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
fun ApplyStatusBarTheme(darkTheme: Boolean = false) {
    val view = LocalView.current

    SideEffect {
        val window = (view.context as ComponentActivity).window
        val insetsController = WindowInsetsControllerCompat(window, view)

        // The top app bar is always the (indigo) primary color, so keep light
        // status-bar icons regardless of theme. Kept as a param for flexibility.
        insetsController.isAppearanceLightStatusBars = false
    }
}
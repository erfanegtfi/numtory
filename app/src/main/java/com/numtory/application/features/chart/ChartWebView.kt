package com.numtory.application.features.chart

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.numtory.application.ui.theme.Primary
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Destination<RootGraph>()
@Composable
fun AppChartWebView(
    htmlContent: String,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(90.dp).background(Primary),
                colors = TopAppBarDefaults.topAppBarColors(// Use 'surface' instead of 'primary' for the app bar background
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "توکن چند",
                            modifier = Modifier.align(Alignment.CenterStart),
                            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimary)
                        )
                    }
                },
                actions = {


                }
            )
        },
    ) { innerPadding ->
    AndroidView(
        modifier = Modifier.fillMaxSize().padding(innerPadding),
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                settings.apply {
                    javaScriptEnabled = true
                    databaseEnabled = true
                    databasePath = context.getDir("databases", Context.MODE_PRIVATE).path

                    // Critical fixes for localStorage
                    domStorageEnabled = true

                    // Enable other storage options
//                    localStorageEnabled = true  // Explicitly enable localStorage
                    mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
//                    // For older APIs
//
                    setSupportZoom(true)
                    loadsImagesAutomatically = true
                    mediaPlaybackRequiresUserGesture = false

                    javaScriptCanOpenWindowsAutomatically = true
                    setSupportMultipleWindows(true)
//
//
                    // File access (be cautious with security)
                    allowFileAccess = false  // Security best practice
                    allowContentAccess = true

                    // Improve compatibility
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    builtInZoomControls = true
                    displayZoomControls = false
                }

                webViewClient = WebViewClient()

                // Load HTML with embedded script
                val fullHtml = """
                     <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                          <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
                    </head>
                    <body >
                    <div style="width: 100%; height: 100vh; overflow: hidden;">
                        $htmlContent
                        </div>
                            </body>
                    </html>
                """.trimIndent()

                loadDataWithBaseURL(null, fullHtml, "text/html", "UTF-8", null)
            }
        },
    )
    }
}
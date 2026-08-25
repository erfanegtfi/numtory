package com.numtory.application.features.scan.presenter.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
 fun ExplorerWebView(
    url: String,
    onWebViewCreated: (WebView) -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    var isLoading by remember { mutableStateOf(true) }

    // A plain holder rather than state: it only guards the load below, and writing to it
    // must not schedule another recomposition.
    val loadedUrl = remember { arrayOfNulls<String>(1) }

    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier.Companion.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        builtInZoomControls = true
                        displayZoomControls = false
                        allowFileAccess = false
                        allowContentAccess = true
                    }

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(
                            view: WebView?,
                            url: String?,
                            favicon: Bitmap?,
                        ) {
                            isLoading = true
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            isLoading = false
                        }
                    }

                    onWebViewCreated(this)
                }
            },
            update = { view ->
                if (loadedUrl[0] != url) {
                    loadedUrl[0] = url
                    view.loadUrl(url)
                }
            }
        )

        if (isLoading)
            LinearProgressIndicator(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .align(Alignment.Companion.TopCenter)
            )
    }
}
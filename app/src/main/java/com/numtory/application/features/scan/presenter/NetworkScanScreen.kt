package com.numtory.application.features.scan.presenter

import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.numtory.application.composeUI.ShowBottomSheet
import com.numtory.application.features.scan.domain.entities.ScanNetwork
import com.numtory.application.features.scan.domain.entities.explorerUrlFor
import com.numtory.application.features.scan.presenter.components.ExplorerWebView
import com.numtory.application.features.scan.presenter.components.NetworkSelector
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination<RootGraph>
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkScanScreen(navigator: DestinationsNavigator) {
    var network by rememberSaveable { mutableStateOf(ScanNetwork.ethereum) }
    var query by rememberSaveable { mutableStateOf("") }
    var loadedUrl by rememberSaveable { mutableStateOf<String?>(null) }
    var showNetworkList by remember { mutableStateOf(false) }
    var webView by remember { mutableStateOf<WebView?>(null) }

    val keyboard = LocalSoftwareKeyboardController.current

    val search: () -> Unit = {
        if (query.isNotBlank()) {
            keyboard?.hide()
            loadedUrl = network.explorerUrlFor(query)
        }
    }

    BackHandler {
        val web = webView
        if (web != null && web.canGoBack()) web.goBack() else navigator.popBackStack()
    }

    if (showNetworkList)
        ShowBottomSheet(onDismiss = { showNetworkList = false }) { _, hide ->
            NetworkListBottomSheetScreen(
                networks = ScanNetwork.entries,
                selected = network,
                hide = hide,
            ) { selected ->
                network = selected
                showNetworkList = false

                if (query.isNotBlank()) loadedUrl = selected.explorerUrlFor(query)
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "اسکنر شبکه",
                        style = MaterialTheme.typography.titleMedium
                            .copy(color = MaterialTheme.colorScheme.onPrimary)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NetworkSelector(
                network = network,
                onClick = { showNetworkList = true },
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )

            // Addresses and hashes are latin text, so they are typed and shown left to right
            // even though the rest of the app is laid out right to left.
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("آدرس کیف پول یا هش تراکنش") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { search() }),
                    trailingIcon = {
                        IconButton(onClick = search) {
                            Icon(Icons.Filled.Search, contentDescription = "Search")
                        }
                    }
                )
            }

            Spacer(Modifier.height(8.dp))

            Box(modifier = Modifier.weight(1f)) {
                val url = loadedUrl
                if (url == null)
                    Text(
                        text = "شبکه را انتخاب کنید و آدرس کیف پول یا هش تراکنش را وارد کنید",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 32.dp)
                    )
                else
                    ExplorerWebView(
                        url = url,
                        onWebViewCreated = { webView = it },
                        modifier = Modifier.fillMaxSize()
                    )
            }
        }
    }
}
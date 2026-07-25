package com.numtory.application.features.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CurrencyBitcoin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.numtory.application.common.cryptoExchangesScreenOpened
import com.numtory.application.common.globalCryptoMarketExchangesScreenOpened
import com.numtory.application.common.goldExchangesScreenOpened
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.cryptoMarket.presenter.CryptoListScreen
import com.numtory.application.features.gold.presenter.GoldMarketList
import com.numtory.application.features.market.presenter.MarketList
import com.numtory.application.features.notification.data.DeepLinkRouter
import com.numtory.application.features.setting.domain.entities.AppSettings
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.MainScreenDestination
import com.ramcosta.composedestinations.generated.destinations.UpdateAppScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

// Define your screen destinations
sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {

    object CryptoExchanges :
        Screen("crypto", "مقایسه قیمت", Icons.Outlined.BarChart, Icons.Filled.Home)

//    object Seke :
//        Screen("seke", "ارز و سکه", Icons.Outlined.AttachMoney, Icons.Filled.AttachMoney)

    object GoldExchanges :
        Screen("gold", "مقایسه طلا", Icons.Outlined.BarChart, Icons.Filled.BarChart)

    object GlobalCryptoMarket :
        Screen(
            "Crypto",
            "قیمت رمزارزها",
            Icons.Outlined.CurrencyBitcoin,
            Icons.Filled.CurrencyBitcoin
        )

    companion object {
        /**
         * Resolves a `route` push-payload value to a tab. Matching is case-sensitive on purpose:
         * CryptoExchanges ("crypto") and GlobalCryptoMarket ("Crypto") differ only by case, so a
         * lenient match would silently send every payload to whichever is listed first.
         */
        fun fromRoute(route: String): Screen? =
            listOf(CryptoExchanges, GoldExchanges, GlobalCryptoMarket)
                .firstOrNull { it.route == route }
    }
}

@Composable
fun SuccessDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Success") },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        },
        icon = {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
            )
        }
    )
}

@Destination<RootGraph>(start = true)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun MainScreen(
    navigator: DestinationsNavigator,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val navController = rememberNavController()

    val settings by viewModel.settingsState.collectAsStateWithLifecycle()
    val dialogMessage by viewModel.showSuccessDialog.collectAsStateWithLifecycle()

    val deepLinkRouter = koinInject<DeepLinkRouter>()
    val pendingRoute by deepLinkRouter.route.collectAsStateWithLifecycle()

    LaunchedEffect(pendingRoute) {
        val route = pendingRoute ?: return@LaunchedEffect
        Screen.fromRoute(route)?.let { screen ->
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
        // Consume regardless, so an unknown route does not re-trigger on every recomposition.
        deepLinkRouter.consume()
    }

//    LaunchedEffect(12) {
//        viewModel.getSettings()
//    }

    if (dialogMessage.showUpdateDialog)
        navigator.navigate(UpdateAppScreenDestination(dialogMessage.serverVersion)) {
            if (dialogMessage.force)
                popUpTo(MainScreenDestination) {
                    inclusive = true
                }
        }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.CryptoExchanges.route,
            modifier = Modifier//.padding(innerPadding)
                .padding(
                    bottom = innerPadding.calculateBottomPadding(), // Only top padding
                    start = innerPadding.calculateStartPadding(LayoutDirection.Rtl),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Rtl)
                    // NO bottom padding!
                )
        ) {
            composable(Screen.CryptoExchanges.route) { MarketList(navigator) }
//            composable(Screen.Seke.route) { SekePriceScreen(navigator) }
            composable(Screen.GoldExchanges.route) { GoldMarketList(navigator) }
            composable(Screen.GlobalCryptoMarket.route) { CryptoListScreen(navigator) }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        modifier = Modifier.height(115.dp),
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround, // Space between items
            verticalAlignment = Alignment.CenterVertically
        ) {
            Material2NavigationBarItem(
                icon = Screen.GlobalCryptoMarket.icon,
                label = Screen.GlobalCryptoMarket.title,
                selected = currentRoute == Screen.GlobalCryptoMarket.route,
                onClick = {
                    globalCryptoMarketExchangesScreenOpened()
                    navController.navigate(Screen.GlobalCryptoMarket.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
            Material2NavigationBarItem(
                icon = Screen.CryptoExchanges.icon,
                label = Screen.CryptoExchanges.title,
                selected = currentRoute == Screen.CryptoExchanges.route,
                onClick = {
                    cryptoExchangesScreenOpened()
                    navController.navigate(Screen.CryptoExchanges.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )

//            Material2NavigationBarItem(
//                icon = Screen.Seke.icon,
//                label = Screen.Seke.title,
//                selected = currentRoute == Screen.Seke.route,
//                onClick = {
//                    sekeScreenOpened()
//                    navController.navigate(Screen.Seke.route) {
//                        popUpTo(navController.graph.findStartDestination().id) {
//                            saveState = true
//                        }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                },
//            )
            Material2NavigationBarItem(
                icon = Screen.GoldExchanges.icon,
                label = Screen.GoldExchanges.title,
                selected = currentRoute == Screen.GoldExchanges.route,
                onClick = {
                    goldExchangesScreenOpened()
                    navController.navigate(Screen.GoldExchanges.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}

@Composable
fun Material2NavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {
    val color = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(horizontal = 8.dp)
            .width(80.dp)
            .fillMaxHeight()
            .clickable(
                onClick = onClick,
//                indication = rememberRipple(
//                    color = MaterialTheme.colorScheme.primary,
//                    radius = 24.dp
//                )
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = color
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            fontSize = 12.sp,
            color = color,
            style = if (selected) MaterialTheme.typography.titleMedium else MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
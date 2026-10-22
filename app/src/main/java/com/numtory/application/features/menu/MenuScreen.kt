package com.numtory.application.features.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.generated.destinations.AboutScreenDestination
import com.ramcosta.composedestinations.generated.destinations.CarPriceScreenDestination
import com.ramcosta.composedestinations.generated.destinations.NetworkScanScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SekePriceScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

private data class MenuItem(
    val title: String,
    val icon: ImageVector,
)

private val menuItems = listOf(
    MenuItem(
        title = "نرخ ارز",
        icon = Icons.Default.CurrencyExchange,
    ),
    MenuItem(
        title = "قیمت خودرو",
        icon = Icons.Default.DirectionsCar,
    ),
    MenuItem(
        title = "اسکنر ارز دیجیتال",
        icon = Icons.Default.QrCodeScanner,
    ),
    MenuItem(
        title = "درباره اپلیکیشن",
        icon = Icons.Default.Info,
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navigator: DestinationsNavigator,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "بازارها",
                        style = MaterialTheme.typography.titleMedium
                            .copy(color = MaterialTheme.colorScheme.onPrimary)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),

            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            items(menuItems.size) { index ->
                MenuItemCard(
                    item = menuItems[index],
                    onClick = {
                        when (index) {
                            0 -> navigator.navigate(SekePriceScreenDestination)
                            1 -> navigator.navigate(CarPriceScreenDestination)
                            2 -> navigator.navigate(NetworkScanScreenDestination)
                            3 -> navigator.navigate(AboutScreenDestination)
                        }
                    },
                )
            }
        }
    }
}


@Composable
private fun MenuItemCard(
    item: MenuItem,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 70.dp)
                .clickable(onClick = onClick)
                .padding(vertical = 6.dp, horizontal = 12.dp),

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                modifier = Modifier.size(26.dp),
            )

            Text(
                text = item.title,
                modifier = Modifier.weight(1f),
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color(0xFF687395),
            )
        }
    }
}

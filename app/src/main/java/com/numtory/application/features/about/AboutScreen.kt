package com.numtory.application.features.about

import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.numtory.application.R
import com.numtory.application.common.aboutScreenOpened
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.ExperimentalCoroutinesApi


@Destination<RootGraph>
@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun AboutScreen(navigator: DestinationsNavigator) {
    val context = LocalContext.current
    LaunchedEffect(12) {
        aboutScreenOpened()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "اطلاعات تماس",
                        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimary)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigator.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(// Use 'surface' instead of 'primary' for the app bar background
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primary
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.launcher_white),
                        contentDescription = "My Image Description",
                        modifier = Modifier
                            .size(80.dp)  // Fixed size
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "توکن چند",
                        style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onPrimary)
                    )
//                    Text(
//                        "Android Developer",
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
                }
            }

            // Contact Items with Copy Functionality
            CopyableContactItem(
                icon = "📧",
                label = "Email",
                value = "tokenchand@gmail.com",
                onCopy = {
                    copyToClipboard(context, "tokenchand@gmail.com")
                }
            )

            CopyableContactItem(
                icon = "💬",
                label = "Telegram",
                value = "@tokenchand",
                onCopy = {
                    copyToClipboard(context, "@tokenchand")
                }
            )

            // Description
            DescriptionWithEditOption(
                description = "اپلیکیشن توکن چند قیمت ارزهای دیجیتال در صرافی های ایرانی را مقایسه می کند. \nقیمت تمامی رمز ارزها و طلا در اپلیکیشن به طور مستقیم از سایت صرافی ها دریافت و نمایش داده می شود.\nقیمت ارز و سکه از طریق سایت های واسط دریافت می شود. \nاپلیکیشن توکن چند هیچ گونه تغییری در قیمت های نمایش داده شده انجام نمی دهد.\nاپلیکیشن توکن چند به طور مستقل هیچ گونه قیمتی را نمایش نمی دهد."
            )
        }
    }
}

@Composable
fun CopyableContactItem(
    icon: String,
    label: String,
    value: String,
    onCopy: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(icon, fontSize = 24.sp)
                Column {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            IconButton(onClick = onCopy) {
                Icon(Icons.Default.ContentCopy, "Copy", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun DescriptionWithEditOption(description: String) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("📝", fontSize = 20.sp)
                Text(
                    "درباره اپلیکیشن",
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            SelectionContainer {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Start,
                )
            }
        }
    }
}

// Helper function to copy to clipboard
private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = android.content.ClipData.newPlainText("Contact Info", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
}
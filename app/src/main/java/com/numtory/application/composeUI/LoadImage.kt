package com.numtory.application.composeUI

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.ImageLoader

import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.numtory.application.R
import org.koin.compose.koinInject

@Composable
fun MyImageLoader(url: String, width: Int = 28, height: Int = 28, modifier: Modifier = Modifier) {
    val imageLoader = koinInject<ImageLoader>()
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
//            .decoderFactory(SvgDecoder.Factory()) // This is the key step
//            .size(100, 100)  // Request specific size
            .build(),
        contentScale = ContentScale.Fit,
        imageLoader = imageLoader,
        contentDescription = "Description of the SVG image",
        // You can still use placeholders and error handlers
        placeholder = painterResource(R.drawable.image_placeholder),
        error = painterResource(R.drawable.image_placeholder),
        modifier = modifier
            .width(width.dp)
            .height(height.dp)
    )
}
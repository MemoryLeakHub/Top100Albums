package com.vrashkov.ui.common

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.imageLoader
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.semantics.Role.Companion.Image
import coil.compose.AsyncImage
import coil.memory.MemoryCache
import com.vrashkov.core.theme.TopAlbumsTheme
enum class LazyImageAnimationType {
    FADE,
    REVEAL_HORIZONTAL,
    REVEAL_BLACK_WHITE
}
@Composable
fun LoadableAsyncImage(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String?,
    placeholderMemoryCacheKey: String? = null,
    loadingIndicatorSize: Dp = 40.dp,
    contentScale: ContentScale = ContentScale.Fit,
    animationType: LazyImageAnimationType = LazyImageAnimationType.REVEAL_BLACK_WHITE
) {
    val imageLoader = LocalContext.current.imageLoader
    var placeholderBitmap by remember(placeholderMemoryCacheKey) { mutableStateOf<Bitmap?>(null) }
    var isLoading by rememberSaveable(model) { mutableStateOf(true) }

    LaunchedEffect(placeholderMemoryCacheKey) {
        placeholderMemoryCacheKey?.let {
            placeholderBitmap =
                imageLoader.memoryCache?.get(MemoryCache.Key(placeholderMemoryCacheKey))?.bitmap
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {

        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = model,
            contentDescription = contentDescription,
            contentScale = contentScale,
            onSuccess = { isLoading = false },
        )

        if(animationType.equals(LazyImageAnimationType.FADE)) {
                AnimatedVisibility(
                    visible = isLoading,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    PlaceholderImage(
                        placeholderBitmap = placeholderBitmap,
                        loadingIndicatorSize = loadingIndicatorSize,
                        contentDescription = contentDescription,
                        contentScale = contentScale
                    )
                }
        } else if (
            animationType.equals(LazyImageAnimationType.REVEAL_HORIZONTAL) ||
            animationType.equals(LazyImageAnimationType.REVEAL_BLACK_WHITE)
        ) {

                val transition = updateTransition(isLoading)
                val reveal by transition.animateFloat(
                    transitionSpec = {
                        tween(durationMillis = 2000, easing = FastOutSlowInEasing)
                    }, label = ""
                ) { isSelected ->
                    if (isSelected) 0f else 1f
                }

                // black and white
                val matrix = ColorMatrix()
                matrix.setToSaturation(0F)

                PlaceholderImage(
                    placeholderImageModifier = Modifier.drawWithContent {
                        clipRect(left = size.width * reveal) {
                            this@drawWithContent.drawContent()
                        }
                    },
                    placeholderBitmap = placeholderBitmap,
                    loadingIndicatorSize = loadingIndicatorSize,
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    colorFilter = if (animationType.equals(LazyImageAnimationType.REVEAL_BLACK_WHITE)) {
                        ColorFilter.colorMatrix(matrix)
                    } else {
                        null
                    }
                )
        }

    }
}

@Composable
private fun BoxScope.PlaceholderImage(
    placeholderImageModifier: Modifier = Modifier,
    placeholderBitmap: Bitmap?,
    loadingIndicatorSize: Dp,
    contentDescription: String?,
    contentScale: ContentScale,
    colorFilter: ColorFilter? = null
) {
    if (placeholderBitmap == null) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center).size(loadingIndicatorSize),
            color = TopAlbumsTheme.colors.buttonPrimary
        )
    } else {
        Image(
            modifier = placeholderImageModifier.fillMaxSize(),
            bitmap = (placeholderBitmap as Bitmap).asImageBitmap(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            colorFilter = colorFilter
        )
    }
}
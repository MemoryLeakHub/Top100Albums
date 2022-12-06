package com.vrashkov.ui.album.single

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vrashkov.core.base.NavigationEvent
import com.vrashkov.core.calculateCurrentSize
import com.vrashkov.core.navigation.Route
import com.vrashkov.core.theme.TopAlbumsTheme
import com.vrashkov.domain.model.AlbumSingle
import com.vrashkov.ui.common.FilledButton
import com.vrashkov.ui.common.LabelButton
import com.vrashkov.ui.common.LoadableAsyncImage
import java.time.format.DateTimeFormatter

@ExperimentalMaterialApi
@Composable
fun AlbumSingleScreen(navController: NavHostController) {

    val isSingle = TopAlbumsTheme.isSingle
    val viewModel = hiltViewModel<AlbumSingleVM>()

    val onTriggerEvents = viewModel::onTriggerEvent
    val viewState = viewModel.viewState.value

    LaunchedEffect(Unit) {
        viewModel.navigationEventFlow.collect {
            when (it) {
                is NavigationEvent.NavigateBack -> {
                    if (isSingle) {
                        navController.navigate(Route.AlbumsList.link) {
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
    }

    AlbumComponent(
        viewModel, viewState, onTriggerEvents, isSingle
    )
}

@Composable
fun AlbumComponent(
    viewModel: AlbumSingleVM,
    viewState: AlbumSingleState,
    onTriggerEvents: (AlbumSingleEvent) -> Unit,
    isSingle: Boolean
) {
    val uriHandler = LocalUriHandler.current
    val album = viewState.album
    val arrowIcon: ImageVector = ImageVector.vectorResource(id = com.vrashkov.core.R.drawable.ic_back_arrow_left)
    val arrowIconPainter = rememberVectorPainter(image = arrowIcon)

    if (album != null)  {
        Column (modifier = Modifier.fillMaxSize().background(color = TopAlbumsTheme.colors.primary)) {
            Box(Modifier.fillMaxWidth().wrapContentHeight()) {
//                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(album.image)
//                        .crossfade(true)
//                        .build(),
//                    contentDescription = null,
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.fillMaxWidth().aspectRatio(1f)
//                )
                LoadableAsyncImage(
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(album.largeImage())
                        .crossfade(true)
                        .build(),
                    contentDescription = "album image",
                    placeholderMemoryCacheKey = album.image,
                )
                if (isSingle) {
                    Box(Modifier.padding(top = 17.dp, start = 16.dp)) {
                        Box(
                            modifier = Modifier.size(32.dp)
                                .clip(CircleShape)
                                .background(TopAlbumsTheme.colors.primary.copy(alpha = 0.5f))
                                .clickable(
                                    onClick = {
                                        onTriggerEvents(AlbumSingleEvent.OnBackClick)
                                    },
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple(
                                        color = TopAlbumsTheme.colors.primary
                                    )
                                )
                        ) {
                            Icon(
                                modifier = Modifier.height(19.dp).align(Alignment.Center),
                                painter = arrowIconPainter,
                                tint = Color.Unspecified,
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 13.dp, bottom = 12.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = album.albumName,
                    style = TopAlbumsTheme.typography.text_style_2.copy(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    ),
                    color = TopAlbumsTheme.colors.labelSecondary
                )
                Text(
                    text = album.artistName,
                    style = TopAlbumsTheme.typography.h1.copy(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    ),
                    color = TopAlbumsTheme.colors.labelPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row (Modifier.fillMaxWidth()){
                    album.genres.forEach {
                        LabelButton(it)
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Column (modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
                Text(
                    text = "Released " + album.releaseDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                    style = TopAlbumsTheme.typography.text_style_2.copy(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    ),
                    color = TopAlbumsTheme.colors.labelSecondary
                )
                Text(
                    text = "Copyright 2022 Apple Inc. All rights reserved.",
                    style = TopAlbumsTheme.typography.text_style_2.copy(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    ),
                    color = TopAlbumsTheme.colors.labelSecondary
                )
                Spacer(modifier = Modifier.height(24.dp))
                FilledButton(
                    label = "Visit The Album",
                    onClick = {
                        uriHandler.openUri(album.albumUrl)
                    }
                )
                Spacer(modifier = Modifier.height(47.dp))
            }
        }
    } else {
        Box (Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.fillMaxSize().align(Alignment.Center),
                text = "Choose an Album",
                style = TopAlbumsTheme.typography.h1,
                textAlign = TextAlign.Center,
                fontSize = 27.sp,
                color = TopAlbumsTheme.colors.labelPrimary
            )
        }
    }
}

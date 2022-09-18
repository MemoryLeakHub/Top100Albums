package com.vrashkov.ui.album.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vrashkov.core.MeasureUnconstrainedViewWidth
import com.vrashkov.core.base.ProgressState
import com.vrashkov.core.calculateCurrentSize
import com.vrashkov.core.getFraction
import com.vrashkov.core.navigation.Route
import com.vrashkov.core.navigation.args
import com.vrashkov.core.theme.TopAlbumsColors
import com.vrashkov.core.theme.TopAlbumsTheme
import com.vrashkov.domain.model.AlbumSingle

@ExperimentalMaterialApi
@Composable
fun AlbumListScreen(navController: NavHostController) {

    val viewModel = hiltViewModel<AlbumListVM>()

    val onTriggerEvents = viewModel::onTriggerEvent
    val viewState = viewModel.viewState.value

    val lazyAlbumList: LazyPagingItems<AlbumSingle> = viewModel.lazyAlbumsItems.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.navigationEventFlow.collect {
            when (it) {
                is AlbumListNavigationEvent.NavigateToAlbumSingle -> {
                    navController.navigate(Route.AlbumsSingle.args(
                        mapOf(
                            "id" to it.id
                        )
                    )) {
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.actionsEventFlow.collect {
            when (it) {
                is AlbumListActionEvent.RefreshAlbumList -> {
                    lazyAlbumList.refresh()
                }
            }
        }
    }

    AlbumListComponent(
        viewModel, viewState, onTriggerEvents, lazyAlbumList
    )
}

@Composable
fun AlbumListComponent(
    viewModel: AlbumListVM,
    viewState: AlbumListState,
    onTriggerEvents: (AlbumListEvent) -> Unit,
    lazyAlbumList: LazyPagingItems<AlbumSingle>
) {
    val lazyGridState = rememberLazyGridState()
    val toolbarLabel: String = "Top 100 Albums"
    var maximumScroll: Float
    var toolbarExpandedHeight: Float
    var toolbarCollapsedHeight: Float

    var toolbarTextSizeExpanded: Float
    var toolbarTextSizeCollapsed: Float

    with(LocalDensity.current) {
        maximumScroll = 30.dp.toPx()

        toolbarExpandedHeight = 65.dp.toPx()
        toolbarCollapsedHeight = 43.dp.toPx()

        toolbarTextSizeExpanded = 34.sp.toPx()
        toolbarTextSizeCollapsed = 16.sp.toPx()
    }

    val currentOffset: Float by remember {
        derivedStateOf {
            val offset = lazyGridState.firstVisibleItemScrollOffset

            if (lazyGridState.firstVisibleItemIndex > 0) {
                maximumScroll
            } else {
                offset.toFloat().coerceIn(0f, maximumScroll)
            }
        }
    }

    var toolbarHeight = 0.dp
    var toolbarHeightPx = 0f
    var toolbarTextSize = 0.sp
    var fraction = 0f
    with(LocalDensity.current) {
        fraction = getFraction(maximumScroll, currentOffset)
        toolbarHeightPx = calculateCurrentSize(toolbarCollapsedHeight, toolbarExpandedHeight, fraction)
        toolbarHeight = toolbarHeightPx.toDp()
        toolbarTextSize = calculateCurrentSize(toolbarTextSizeCollapsed, toolbarTextSizeExpanded, fraction).toSp()
    }

    val lazyAlbumsIsRefreshing = lazyAlbumList.loadState.refresh is LoadState.Loading

    var swipeEnabled by remember { mutableStateOf(false)}
    var loadingProgressBarEnabled by remember { mutableStateOf(false)}
    // we make sure the swipe message appears only if the lazy results are finished loading and return empty
    swipeEnabled = !lazyAlbumsIsRefreshing && lazyAlbumList.itemCount <= 0 && viewState.networkError

    // we make sure the loader appear only when the lazy results finished loading and have no items in them
    // only than if progress has state of ProgressBarState.Loading it will be loading
    loadingProgressBarEnabled = viewState.progress == ProgressState.Loading && (!lazyAlbumsIsRefreshing && lazyAlbumList.itemCount <= 0)

    Box (modifier = Modifier.fillMaxSize().background(color = TopAlbumsTheme.colors.primary)) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(lazyAlbumsIsRefreshing),
            onRefresh = {
                onTriggerEvents(AlbumListEvent.SwipeRefresh)
            },
            swipeEnabled = swipeEnabled
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(2), state = lazyGridState,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 15.dp + 65.dp,
                    bottom = 40.dp
                )
            ) {
                items(lazyAlbumList.itemCount) { index ->
                    val data = lazyAlbumList.get(index)
                    data?.let {
                        AlbumItem(
                            album = data,
                            onAlbumClick = {
                                onTriggerEvents(AlbumListEvent.OnAlbumClick(data.id))
                            }
                        )
                    }
                }
            }
        }

        Column (modifier = Modifier.fillMaxWidth()){
            Box (modifier = Modifier
                .fillMaxWidth()
                .height(toolbarHeight)) {

                Box(
                    modifier = Modifier.fillMaxSize()
                    .background(color = TopAlbumsTheme.colors.primary.copy(alpha = 0.9f))
                )

                Box(modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)) {
                    MeasureUnconstrainedViewWidth(
                        viewToMeasure = {
                            // Calculating the end text size so we can animate it properly
                            Text(
                                text = toolbarLabel,
                                style = TopAlbumsTheme.typography.h1.copy(
                                    platformStyle = PlatformTextStyle(
                                        includeFontPadding = false
                                    )
                                ),
                                fontSize = toolbarTextSize
                            )
                        }
                    ) { measuredEndWidth, measuredEndHeight ->
                        // Our text view that we are animating based on the returned measured "end" values
                        Text(
                            modifier = Modifier.layout { measurable, constraints ->
                                val placeable = measurable.measure(constraints)

                                layout(constraints.maxWidth, constraints.maxHeight) {
                                    val xOffsetEnd =
                                        (constraints.maxWidth / 2) - (measuredEndWidth / 2)

                                    val xOffset =
                                        calculateCurrentSize(xOffsetEnd.toFloat(), 0f, fraction)

                                    val yOffset = (toolbarHeightPx / 2) - (placeable.height / 2)
                                    placeable.placeRelative(xOffset.toInt(), yOffset.toInt())
                                }
                            },
                            text = toolbarLabel,
                            style = TopAlbumsTheme.typography.h1.copy(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            ),
                            fontSize = toolbarTextSize,
                            color = TopAlbumsTheme.colors.labelPrimary
                        )
                    }
                }
            }
            AnimatedVisibility(swipeEnabled, enter = fadeIn(), exit = fadeOut()) {
                Row (modifier = Modifier.fillMaxWidth().background(color = TopAlbumsTheme.colors.error), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Text(
                        modifier = Modifier.padding(vertical = 6.dp),
                        text = "Something went wrong! Swipe to Refresh",
                        style = TopAlbumsTheme.typography.body_3,
                        color = TopAlbumsTheme.colors.labelPrimaryLight
                    )
                }
            }
        }

        AnimatedVisibility(loadingProgressBarEnabled, enter = fadeIn(), exit = fadeOut()) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = TopAlbumsTheme.colors.buttonPrimary
                )
            }
        }
    }
}

@Composable
private fun AlbumItem(
    album: AlbumSingle,
    onAlbumClick: (album: AlbumSingle) -> Unit
) {
    Box (modifier = Modifier.fillMaxWidth().aspectRatio(1f)
        .clip(shape = RoundedCornerShape(20.dp))) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(album.image)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            Modifier.fillMaxSize().background(
            brush =
                Brush.verticalGradient(
                    0.49F to TopAlbumsColors.black.copy(alpha = 0f),
                    1F to TopAlbumsColors.black.copy(alpha = 0.75f)
                )
            ).clickable(
                onClick = {onAlbumClick(album)},
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    color = TopAlbumsTheme.colors.primary
                )
            )
        )

        Column(modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(start = 12.dp, end = 13.dp, bottom = 12.dp)
        ) {
            Text(
                text = album.albumName,
                style = TopAlbumsTheme.typography.text_style_2.copy(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                ),
                color = TopAlbumsTheme.colors.labelPrimaryLight
            )
            Text(
                text = album.artistName,
                style = TopAlbumsTheme.typography.text_style.copy(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                ),
                color = TopAlbumsTheme.colors.labelSecondary
            )
        }
    }
}
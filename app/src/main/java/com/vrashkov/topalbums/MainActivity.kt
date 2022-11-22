package com.vrashkov.topalbums

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.vrashkov.core.navigation.Route
import com.vrashkov.core.theme.TopAlbumsTheme
import com.vrashkov.ui.album.list.AlbumListScreen
import com.vrashkov.ui.album.single.AlbumSingleScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TopAlbumsTheme {
                AppWrapper()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun AppWrapper() {
    val navController = rememberAnimatedNavController()

    if (TopAlbumsTheme.isSingle) {
        AnimatedNavHost(
            navController = navController,
            startDestination = Route.AlbumsList.link
        ) {

            composable(
                Route.AlbumsList.link
            ) {
                AlbumListScreen(navController = navController)
            }
            composable(
                Route.AlbumsSingle.link
            ) {
                AlbumSingleScreen(navController = navController)
            }
        }
    } else {
        Row (modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.weight(1f)) {
                AlbumListScreen(navController = navController)
            }
            LazyColumn(modifier = Modifier
                    .weight(1f)
                    .background(color = TopAlbumsTheme.colors.primary)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    AlbumSingleScreen(navController = navController)
                }
            }
        }
    }
}
package com.vrashkov.topalbums

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
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
}
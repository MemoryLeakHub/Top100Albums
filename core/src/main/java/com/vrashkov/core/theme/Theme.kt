package com.vrashkov.core.theme

import android.content.res.Resources
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vrashkov.core.theme.TopAlbumsTheme.typography

fun lightColors(): AppColors = AppColors(
    primary = TopAlbumsColors.white,

    labelPrimary = TopAlbumsColors.dark,
    labelPrimaryLight = TopAlbumsColors.white,

    labelSecondary = TopAlbumsColors.gray,

    buttonPrimary = TopAlbumsColors.blue,

    error = TopAlbumsColors.red
)

fun darkColors(): AppColors = AppColors(
    primary = TopAlbumsColors.dark,

    labelPrimary = TopAlbumsColors.white,
    labelPrimaryLight = TopAlbumsColors.white,

    labelSecondary = TopAlbumsColors.gray,

    buttonPrimary = TopAlbumsColors.blue,

    error = TopAlbumsColors.red
)

internal val localColors = staticCompositionLocalOf { lightColors() }

object TopAlbumsTheme {

    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = localColors.current


    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

}

@Composable
fun TopAlbumsTheme(
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(if (isSystemInDarkTheme) darkColors().primary else lightColors().primary)
    systemUiController.isStatusBarVisible = true
    systemUiController.statusBarDarkContentEnabled = !isSystemInDarkTheme
    systemUiController.setNavigationBarColor(if (isSystemInDarkTheme) darkColors().primary else lightColors().primary)
    systemUiController.navigationBarDarkContentEnabled = !isSystemInDarkTheme
    //systemUiController.setSystemBarsColor(color = Color.Transparent)
    val rememberedColors = remember { if (isSystemInDarkTheme) darkColors() else lightColors() }

    CompositionLocalProvider(
        localColors provides rememberedColors,
        LocalTypography provides typography
    ) {
        ProvideTextStyle(value = typography.text_style, content = content)
    }

}
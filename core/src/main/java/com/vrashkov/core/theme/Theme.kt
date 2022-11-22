package com.vrashkov.core.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.content.res.Resources
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vrashkov.core.theme.TopAlbumsTheme.isSingle
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
internal val localLayoutType = staticCompositionLocalOf { true }
object TopAlbumsTheme {

    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = localColors.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    val isSingle: Boolean
        @Composable
        @ReadOnlyComposable
        get() = localLayoutType.current
}
@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = originalOrientation
        }
    }
}
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
@Composable
fun TopAlbumsTheme(
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val configuration = LocalConfiguration.current
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(if (isSystemInDarkTheme) darkColors().primary else lightColors().primary)
    systemUiController.isStatusBarVisible = true
    systemUiController.statusBarDarkContentEnabled = !isSystemInDarkTheme
    systemUiController.setNavigationBarColor(if (isSystemInDarkTheme) darkColors().primary else lightColors().primary)
    systemUiController.navigationBarDarkContentEnabled = !isSystemInDarkTheme
    //systemUiController.setSystemBarsColor(color = Color.Transparent)
    val rememberedColors = remember { if (isSystemInDarkTheme) darkColors() else lightColors() }
    val rememberLayoutType = remember { configuration.screenWidthDp < 600 }
    if (configuration.screenWidthDp < 600) {
        LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    } else {
        LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
    }
    CompositionLocalProvider(
        localColors provides rememberedColors,
        LocalTypography provides typography,
        localLayoutType provides rememberLayoutType
    ) {
        ProvideTextStyle(value = typography.text_style, content = content)
    }

}
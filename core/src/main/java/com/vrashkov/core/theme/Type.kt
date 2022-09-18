package com.vrashkov.core.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.PlatformParagraphStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.vrashkov.core.R

private val SFPro = FontFamily(
    Font(R.font.sf_pro_display_bold, FontWeight.W700),
    Font(R.font.sf_pro_text_semibold, FontWeight.W600),
    Font(R.font.sf_pro_text_medium, FontWeight.W500),
    Font(R.font.sf_pro_text_regular, FontWeight.W400)
)
private val Roboto = FontFamily(
    Font(R.font.roboto_medium, FontWeight.W500),
    Font(R.font.roboto_regular, FontWeight.W400)
)
data class AppTypography(
    val h1: TextStyle = TextStyle(
        fontFamily = SFPro,
        fontWeight = FontWeight.W700,
        fontSize = 34.sp,
        letterSpacing = -1.36.sp
    ),
    val h2: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W500,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    val body_1: TextStyle = TextStyle(
        fontFamily = SFPro,
        fontWeight = FontWeight.W400,
        letterSpacing = -0.72.sp
    ),
    val body_3: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    val subtitle: TextStyle = TextStyle(
        fontFamily = SFPro,
        fontWeight = FontWeight.W700,
        fontSize = 16.sp,
        letterSpacing = -0.64.sp
    ),
    val text_style:TextStyle = TextStyle(
        fontFamily = SFPro,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp
    ),
    val text_style_2:TextStyle = TextStyle(
        fontFamily = SFPro,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        letterSpacing = -0.64.sp
    ),
    val button: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.25.sp
    )
)

internal val LocalTypography = staticCompositionLocalOf { AppTypography() }
package com.vrashkov.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.vrashkov.core.theme.TopAlbumsTheme

@Composable
fun LabelButton(
    label: String,
) {
    Row (modifier = Modifier
        .border(border = BorderStroke(width = 1.dp, color = TopAlbumsTheme.colors.buttonPrimary), shape =RoundedCornerShape(50.dp) )
    ){
        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            text = label,
            style = TopAlbumsTheme.typography.text_style,
            color = TopAlbumsTheme.colors.buttonPrimary
        )
    }
}
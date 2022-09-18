package com.vrashkov.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.vrashkov.core.theme.TopAlbumsTheme

@Composable
fun FilledButton(
    label: String,
    onClick: () -> Unit
) {
    Row (modifier = Modifier
        .background(color = TopAlbumsTheme.colors.buttonPrimary, shape = RoundedCornerShape(10.dp))
        .clip(RoundedCornerShape(10.dp))
        .clickable(
            onClick = onClick,
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(
                color = TopAlbumsTheme.colors.primary
            )
        ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 13.dp),
            text = label,
            style = TopAlbumsTheme.typography.text_style_2,
            color = TopAlbumsTheme.colors.primary
        )
    }
}
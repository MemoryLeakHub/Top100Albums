package com.vrashkov.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints


fun getFraction(max: Float,  currentOffset: Float) : Float {
    val fraction = 1f - (currentOffset/max)
    return fraction
}
fun calculateCurrentSize(min: Float, max: Float,  fraction: Float): Float {

    if (fraction == 1f) {
        return max
    } else if (fraction == 0f) {
        return min
    }

    val result =  min + ((max - min) * fraction)
    return result
}

@Composable
fun MeasureUnconstrainedViewWidth(
    viewToMeasure: @Composable () -> Unit,
    content: @Composable (measuredWidth: Int, measuredHeight: Int) -> Unit,
) {
    SubcomposeLayout { constraints ->
        val measured = subcompose("viewToMeasure", viewToMeasure)[0]
            .measure(Constraints())

        val measuredWidth = measured.width
        val measuredHeight = measured.height

        val contentPlaceable = subcompose("content") {
            content(measuredWidth, measuredHeight)
        }[0].measure(constraints)
        layout(contentPlaceable.width, contentPlaceable.height) {
            contentPlaceable.place(0, 0)
        }
    }
}
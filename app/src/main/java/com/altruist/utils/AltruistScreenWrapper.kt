package com.altruist.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.unit.dp
import com.altruist.ui.theme.BackgroundBottom
import com.altruist.ui.theme.BackgroundTop
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AltruistScreenWrapper(
    modifier: Modifier = Modifier,
    color: Color,
    useDarkIcons: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable BoxScope.() -> Unit
) {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = color,
            darkIcons = useDarkIcons
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to BackgroundTop,
                        0.6f to BackgroundTop,
                        1.0f to BackgroundBottom
                    )
                )
            ),
        content = content
    )
}

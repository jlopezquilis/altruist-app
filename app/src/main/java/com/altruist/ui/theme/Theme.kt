package com.altruist.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = DarkYellow,
    secondary = YellowLight,
    background = BackgroundTop,
    onPrimary = Black,
    onSecondary = Black,
)

@Composable
fun AltruistTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

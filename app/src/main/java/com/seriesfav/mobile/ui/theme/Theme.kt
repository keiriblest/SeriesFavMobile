package com.seriesfav.mobile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val RedAccent   = Color(0xFFE50914)
val BgDark      = Color(0xFF0D0D0D)
val SurfaceDark = Color(0xFF1C1C1C)
val TextMuted   = Color(0xFF888888)

private val DarkColors = darkColorScheme(
    background   = BgDark,
    surface      = SurfaceDark,
    primary      = RedAccent,
    onBackground = Color.White,
    onSurface    = Color(0xFFCCCCCC)
)

@Composable
fun SeriesFavTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DarkColors, content = content)
}

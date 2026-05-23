package com.seriesfav.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.seriesfav.mobile.ui.navigation.NavGraph
import com.seriesfav.mobile.ui.theme.SeriesFavTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            SeriesFavTheme {
                NavGraph()
            }
        }
    }
}

package com.seriesfav.mobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.seriesfav.mobile.ui.screens.DetailScreen
import com.seriesfav.mobile.ui.screens.HomeScreen
import com.seriesfav.mobile.ui.screens.PlayerScreen
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun NavGraph() {
    val nav = rememberNavController()
    NavHost(nav, startDestination = "home") {

        composable("home") {
            HomeScreen(onSerieClick = { i -> nav.navigate("detail/$i") })
        }

        composable(
            "detail/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { back ->
            val idx = back.arguments?.getInt("index") ?: 0
            DetailScreen(
                serieIndex = idx,
                onPlayClick = { url ->
                    nav.navigate("player/" + URLEncoder.encode(url, "UTF-8"))
                },
                onBack = { nav.popBackStack() }
            )
        }

        composable(
            "player/{url}",
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { back ->
            val url = URLDecoder.decode(back.arguments?.getString("url") ?: "", "UTF-8")
            PlayerScreen(sourceUrl = url, onBack = { nav.popBackStack() })
        }
    }
}

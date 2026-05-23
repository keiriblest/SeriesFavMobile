package com.seriesfav.mobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.seriesfav.mobile.data.model.Serie
import com.seriesfav.mobile.ui.theme.*
import com.seriesfav.mobile.ui.viewmodel.HomeUiState
import com.seriesfav.mobile.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onSerieClick: (Int) -> Unit, vm: HomeViewModel = viewModel()) {
    val state  by vm.state.collectAsState()
    val tabs    = listOf("Todas", "Vistas", "Viendo", "Pendiente")
    var active by remember { mutableStateOf("Todas") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("SeriesFav", fontWeight = FontWeight.ExtraBold,
                         color = RedAccent, fontSize = 22.sp)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgDark)
            )
        },
        containerColor = BgDark
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            ScrollableTabRow(
                selectedTabIndex = tabs.indexOf(active),
                containerColor   = BgDark,
                edgePadding      = 12.dp,
                divider          = {}
            ) {
                tabs.forEach { tab ->
                    Tab(
                        selected = active == tab,
                        onClick  = { active = tab },
                        text = {
                            Text(
                                tab,
                                color      = if (active == tab) RedAccent else TextMuted,
                                fontWeight = if (active == tab) FontWeight.Bold else FontWeight.Normal,
                                fontSize   = 13.sp
                            )
                        }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            when (state) {
                is HomeUiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator(color = RedAccent)
                }
                is HomeUiState.Error -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text((state as HomeUiState.Error).message, color = Color.White)
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = { vm.load() },
                            colors  = ButtonDefaults.buttonColors(containerColor = RedAccent)
                        ) { Text("Reintentar") }
                    }
                }
                is HomeUiState.Success -> {
                    val all = (state as HomeUiState.Success).series
                    val filtered = if (active == "Todas") all
                                   else all.filter { it.seccion.equals(active, ignoreCase = true) }
                    SeriesGrid(filtered, all, onSerieClick)
                }
            }
        }
    }
}

@Composable
private fun SeriesGrid(series: List<Serie>, all: List<Serie>, onClick: (Int) -> Unit) {
    LazyVerticalGrid(
        columns               = GridCells.Fixed(3),
        contentPadding        = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement   = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(series) { _, s ->
            SerieCard(s) { onClick(all.indexOf(s)) }
        }
    }
}

@Composable
private fun SerieCard(s: Serie, onClick: () -> Unit) {
    Column(
        Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDark)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model              = s.imgURL,
            contentDescription = s.titulo,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
        )
        Column(Modifier.padding(7.dp)) {
            Text(s.titulo, color = Color.White, fontSize = 11.sp,
                 fontWeight = FontWeight.SemiBold, maxLines = 1)
            Text(s.temporada, color = TextMuted, fontSize = 10.sp)
            Spacer(Modifier.height(4.dp))
            PlatformBadge(s.plataforma)
        }
    }
}

@Composable
private fun PlatformBadge(platform: String) {
    val color = when (platform.lowercase()) {
        "netflix"     -> Color(0xFFE50914)
        "hbo", "max"  -> Color(0xFF0047BB)
        "disney+"     -> Color(0xFF113CCF)
        "prime video" -> Color(0xFF00A8E1)
        "apple tv+"   -> Color(0xFF555555)
        else          -> Color(0xFF444444)
    }
    Box(
        Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color)
            .padding(horizontal = 5.dp, vertical = 2.dp)
    ) {
        Text(platform, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
    }
}

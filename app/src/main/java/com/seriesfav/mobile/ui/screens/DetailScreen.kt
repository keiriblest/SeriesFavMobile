package com.seriesfav.mobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.seriesfav.mobile.data.model.VerItem  // ← CAMBIADO de Capitulo a VerItem
import com.seriesfav.mobile.ui.theme.*
import com.seriesfav.mobile.ui.viewmodel.HomeUiState
import com.seriesfav.mobile.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    serieIndex: Int,
    onPlayClick: (String) -> Unit,
    onBack: () -> Unit,
    vm: HomeViewModel = viewModel()
) {
    val state = vm.state.collectAsState().value
    val serie = (state as? HomeUiState.Success)?.series?.getOrNull(serieIndex) ?: return

    Scaffold(containerColor = BgDark) { padding ->
        LazyColumn(Modifier.padding(padding)) {

            item {
                Box(Modifier.fillMaxWidth().height(320.dp)) {
                    AsyncImage(
                        model              = serie.imgURL,
                        contentDescription = serie.titulo,
                        contentScale       = ContentScale.Crop,
                        modifier           = Modifier.fillMaxSize()
                    )
                    Box(Modifier.fillMaxSize().background(
                        Brush.verticalGradient(listOf(Color.Transparent, BgDark), startY = 200f)
                    ))
                    IconButton(
                        onClick  = onBack,
                        modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, "Volver", tint = Color.White)
                    }
                    Column(Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                        Text(serie.titulo, color = Color.White, fontSize = 24.sp,
                             fontWeight = FontWeight.ExtraBold)
                        Text("${serie.plataforma} · ${serie.temporada}",
                             color = TextMuted, fontSize = 13.sp)
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    SectionBadge(serie.seccion)
                    Spacer(Modifier.height(12.dp))
                    Text(serie.descripcion, color = Color(0xFFCCCCCC),
                         fontSize = 14.sp, lineHeight = 22.sp)
                    Spacer(Modifier.height(20.dp))
                    Text("Capítulos", color = Color.White, fontSize = 17.sp,
                         fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                }
            }

            itemsIndexed(serie.verURL) { i, cap ->
                CapituloRow(i + 1, cap) { onPlayClick(cap.url) }
            }

            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}

@Composable
private fun SectionBadge(seccion: String) {
    val color = when (seccion.lowercase()) {
        "vistas"    -> Color(0xFF27AE60)
        "viendo"    -> Color(0xFFF39C12)
        "pendiente" -> Color(0xFF2980B9)
        else        -> Color(0xFF555555)
    }
    Box(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            seccion.replaceFirstChar { it.uppercase() },
            color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CapituloRow(number: Int, cap: VerItem, onClick: () -> Unit) {  // ← CAMBIADO
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceDark)
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(36.dp).clip(RoundedCornerShape(8.dp))
                    .background(RedAccent.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text("$number", color = RedAccent, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                // Muestra "T1 · C3" si existen, o solo la URL si es un item simple
                text = if (cap.temporada.isNotBlank() || cap.capitulo.isNotBlank())
                           "${cap.temporada} · ${cap.capitulo}"
                       else
                           "Reproducir",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Icon(Icons.Default.PlayArrow, null, tint = RedAccent, modifier = Modifier.size(22.dp))
    }
}

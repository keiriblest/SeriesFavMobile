package com.seriesfav.mobile.ui.screens

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.PlayerView
import com.seriesfav.mobile.ui.theme.RedAccent
import com.seriesfav.mobile.ui.viewmodel.PlayerState
import com.seriesfav.mobile.ui.viewmodel.PlayerViewModel
import kotlinx.coroutines.delay

@SuppressLint("SetJavaScriptEnabled")
@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(
    sourceUrl: String,
    onBack: () -> Unit,
    vm: PlayerViewModel = viewModel()
) {
    val context = LocalContext.current
    val state   by vm.state.collectAsState()
    var webView by remember { mutableStateOf<WebView?>(null) }

    LaunchedEffect(sourceUrl) {
        vm.reset()
        delay(20_000L)
        if (vm.state.value is PlayerState.Extracting) {
            vm.setError("No se pudo extraer el stream M3U8.\nIntenta con otro capítulo.")
        }
    }

    Box(Modifier.fillMaxSize().background(Color.Black)) {

        when (val s = state) {

            is PlayerState.Extracting -> {
                AndroidView(
                    factory = { ctx ->
                        WebView(ctx).also { wv ->
                            webView = wv
                            wv.layoutParams = ViewGroup.LayoutParams(1, 1)
                            wv.settings.apply {
                                javaScriptEnabled                = true
                                domStorageEnabled                = true
                                mixedContentMode                 = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                                userAgentString                  = "Mozilla/5.0 (Linux; Android 12; Pixel 6) AppleWebKit/537.36 Chrome/112.0.0.0 Mobile Safari/537.36"
                                mediaPlaybackRequiresUserGesture = false
                            }
                            wv.webViewClient = vm.makeWebViewClient()
                            wv.loadUrl(sourceUrl)
                        }
                    },
                    modifier = Modifier.size(1.dp)
                )
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement   = Arrangement.Center,
                    horizontalAlignment   = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = RedAccent, strokeWidth = 3.dp)
                    Spacer(Modifier.height(20.dp))
                    Text("Extrayendo stream...", color = Color.White)
                    Spacer(Modifier.height(8.dp))
                    Text("Sin anuncios · Calidad máxima",
                         color = Color(0xFF888888),
                         style = MaterialTheme.typography.bodySmall)
                }
            }

            is PlayerState.Ready -> {
                DisposableEffect(Unit) { onDispose { webView?.destroy() } }

                val player = remember {
                    ExoPlayer.Builder(context).build().apply {
                        val src = HlsMediaSource.Factory(
                            DefaultHttpDataSource.Factory().setUserAgent("Mozilla/5.0")
                        ).createMediaSource(MediaItem.fromUri(s.url))
                        setMediaSource(src)
                        prepare()
                        playWhenReady = true
                    }
                }
                DisposableEffect(player) { onDispose { player.release() } }

                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            this.player   = player
                            useController = true
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            is PlayerState.Error -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement   = Arrangement.Center,
                    horizontalAlignment   = Alignment.CenterHorizontally
                ) {
                    Text(s.msg, color = Color.White, textAlign = TextAlign.Center,
                         modifier = Modifier.padding(horizontal = 32.dp))
                    Spacer(Modifier.height(20.dp))
                    Button(
                        onClick = onBack,
                        colors  = ButtonDefaults.buttonColors(containerColor = RedAccent)
                    ) { Text("Volver") }
                }
            }
        }

        IconButton(
            onClick  = onBack,
            modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
        ) {
            Icon(Icons.Default.ArrowBack, "Volver", tint = Color.White)
        }
    }
}

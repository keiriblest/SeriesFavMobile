package com.seriesfav.mobile.ui.viewmodel

import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class PlayerState {
    object Extracting                 : PlayerState()
    data class Ready(val url: String) : PlayerState()
    data class Error(val msg: String) : PlayerState()
}

class PlayerViewModel : ViewModel() {
    private val _state = MutableStateFlow<PlayerState>(PlayerState.Extracting)
    val state: StateFlow<PlayerState> = _state

    fun reset()               { _state.value = PlayerState.Extracting }
    fun setReady(url: String) { if (_state.value is PlayerState.Extracting) _state.value = PlayerState.Ready(url) }
    fun setError(msg: String) { _state.value = PlayerState.Error(msg) }

    fun makeWebViewClient(): WebViewClient = object : WebViewClient() {
        override fun shouldInterceptRequest(
            view: WebView, request: WebResourceRequest
        ): WebResourceResponse? {
            val url = request.url.toString()
            if (url.contains(".m3u8") ||
                (url.contains("playlist") && url.startsWith("http")) ||
                url.contains("index.m3u8") ||
                url.contains("master.m3u8")) {
                setReady(url)
            }
            return super.shouldInterceptRequest(view, request)
        }
    }
}

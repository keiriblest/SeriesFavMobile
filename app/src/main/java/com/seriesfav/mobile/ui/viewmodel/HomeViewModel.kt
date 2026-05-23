package com.seriesfav.mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seriesfav.mobile.data.model.Serie
import com.seriesfav.mobile.data.repository.SeriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Loading                              : HomeUiState()
    data class Success(val series: List<Serie>) : HomeUiState()
    data class Error(val message: String)       : HomeUiState()
}

class HomeViewModel : ViewModel() {
    private val repo   = SeriesRepository()
    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state: StateFlow<HomeUiState> = _state

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = HomeUiState.Loading
            repo.getSeries()
                .onSuccess  { _state.value = HomeUiState.Success(it) }
                .onFailure  { _state.value = HomeUiState.Error(it.message ?: "Error al cargar") }
        }
    }
}

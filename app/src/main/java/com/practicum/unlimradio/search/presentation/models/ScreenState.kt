package com.practicum.unlimradio.search.presentation.models

import com.practicum.unlimradio.search.domain.model.Station

sealed class ScreenState {
    data object Default: ScreenState()
    data object Loading: ScreenState()
    data class Content(val stations: List<Station>): ScreenState()
    data class Error(val message: String): ScreenState()
    data object Empty: ScreenState()
}
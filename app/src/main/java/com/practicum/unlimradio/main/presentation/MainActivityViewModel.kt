package com.practicum.unlimradio.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.unlimradio.favorites.domain.api.FavoritesInteractor
import com.practicum.unlimradio.search.domain.model.Station
import com.practicum.unlimradio.search.presentation.models.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private var _favoriteStationListState =
        MutableStateFlow<ScreenState>(ScreenState.Loading)
    val favoriteStationListState = _favoriteStationListState.asStateFlow()

    fun getFavoriteStations() {
        viewModelScope.launch {
            favoritesInteractor.getFavoriteStations().firstOrNull { favoriteStations ->
                handleFavoriteStations(favoriteStations)
                true
            }
        }
    }

    private fun handleFavoriteStations(favoriteStations: List<Station>) {
        _favoriteStationListState.value = if (favoriteStations.isEmpty()) {
            ScreenState.Empty
        } else {
            ScreenState.Content(favoriteStations)
        }
    }

    fun insertStationToFavorite(station: Station) {
        viewModelScope.launch {
            if (favoritesInteractor.insertStationToFavorite(station) > 0) {
                getFavoriteStations()
            }
        }
    }

    fun deleteStationFromFavorite(station: Station) {
        viewModelScope.launch {
            if (favoritesInteractor.deleteStationFromFavoriteById(station.stationUuid) > 0) {
                getFavoriteStations()
            }
        }
    }
}
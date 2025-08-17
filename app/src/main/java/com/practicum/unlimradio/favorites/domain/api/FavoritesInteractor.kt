package com.practicum.unlimradio.favorites.domain.api

import com.practicum.unlimradio.search.domain.model.Station
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
        suspend fun insertStationToFavorite(station: Station): Long
        suspend fun getFavoriteStations(): Flow<List<Station>>
        suspend fun deleteStationFromFavoriteById(stationUuid: String): Int
}
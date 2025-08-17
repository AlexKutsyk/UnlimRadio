package com.practicum.unlimradio.favorites.domain.impl

import com.practicum.unlimradio.favorites.domain.api.FavoritesInteractor
import com.practicum.unlimradio.favorites.domain.api.FavoritesRepository
import com.practicum.unlimradio.search.domain.model.Station
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesInteractorImpl @Inject constructor(
    private val favoritesRepository: FavoritesRepository
): FavoritesInteractor {
    override suspend fun insertStationToFavorite(station: Station): Long {
        return favoritesRepository.insertStationToFavorite(station)
    }

    override suspend fun getFavoriteStations(): Flow<List<Station>> {
        return favoritesRepository.getFavoriteStations()
    }

    override suspend fun deleteStationFromFavoriteById(stationUuid: String): Int {
        return favoritesRepository.deleteStationFromFavoriteById(stationUuid)
    }
}
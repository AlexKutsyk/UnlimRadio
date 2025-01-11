package com.practicum.unlimradio.favorites.data

import com.practicum.unlimradio.favorites.data.converter.StationConverter
import com.practicum.unlimradio.favorites.data.entity.StationEntity
import com.practicum.unlimradio.favorites.domain.api.FavoritesRepository
import com.practicum.unlimradio.search.domain.model.Station
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val converter: StationConverter
) : FavoritesRepository {
    override suspend fun insertStationToFavorite(station: Station): Long {
        return appDatabase.stationDao().insertStationToFav(converter.mapToEntity(station))
    }

    override suspend fun getFavoriteStations(): Flow<List<Station>> = flow {
        val favoriteStations = appDatabase.stationDao().getFavStations()
        emit(convertListFromEntity(favoriteStations))
    }

    override suspend fun deleteStationFromFavoriteById(stationUuid: String): Int {
        return appDatabase.stationDao().deleteStationFromFavById(stationUuid)
    }

    private fun convertListFromEntity(stationsEntity: List<StationEntity>): List<Station> {
        return stationsEntity.map { converter.mapFromEntity(it) }
    }
}
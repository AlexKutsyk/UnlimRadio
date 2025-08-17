package com.practicum.unlimradio.extraStation.data

import com.practicum.unlimradio.favorites.domain.api.FavoritesRepository
import com.practicum.unlimradio.search.domain.model.Station
import com.practicum.unlimradio.extraStation.domain.api.ExtraStationRepository
import javax.inject.Inject

class ExtraStationRepositoryImpl @Inject constructor(
    val favoritesRepository: FavoritesRepository
) : ExtraStationRepository {
    var amountRequest = 0
    var startTime = 0L

    override suspend fun getExtraStation(): Boolean {
        return if (checkIsValidRequest() && amountRequest == TARGET) {
            favoritesRepository.insertStationToFavorite(
                Station(
                    stationUuid = "tbs",
                    name = "TBS Radio",
                    url = "https://91.235.234.164:8443/main",
                    urlResolved = "https://91.235.234.164:8443/main",
                    favicon = "https://merchmag.ru/images/logos/3/plushev-logo-300.png",
                    country = "Russia",
                    countryCode = "RU",
                    language = "Russian",
                    codec = "M3U",
                    bitrate = 96
                )
            ) != 0L
        } else {
            false
        }
    }

    private fun checkIsValidRequest(): Boolean {
        val currentTime = System.currentTimeMillis()
        return if (currentTime - startTime < RUN_TIME) {
            amountRequest++
            true
        } else {
            amountRequest = 1
            startTime = currentTime
            false
        }
    }

    companion object {
        private const val RUN_TIME = 10_000L
        private const val TARGET = 7
    }
}
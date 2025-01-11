package com.practicum.unlimradio.favorites.data.converter

import android.graphics.Bitmap
import com.google.gson.Gson
import com.practicum.unlimradio.favorites.data.entity.StationEntity
import com.practicum.unlimradio.search.domain.model.Station
import javax.inject.Inject

class StationConverter @Inject constructor() {

    fun mapToEntity(station: Station): StationEntity {
        return with(station) {
            StationEntity(
                stationUuid = stationUuid,
                name = name,
                url = url,
                urlResolved = urlResolved,
                favIcon = favicon,
                tags = tags,
                country = country,
                countryCode = countryCode,
                language = language,
                codec = codec,
                bitrate = bitrate,
                lastCheckOk = lastCheckOk,
                lastCheckTime = lastCheckTime,
                isFavorite = true
            )
        }
    }

    fun mapFromEntity(stationEntity: StationEntity) : Station {
        return with(stationEntity) {
            Station(
                stationUuid = stationUuid,
                name = name,
                url = url,
                urlResolved = urlResolved,
                favicon = favIcon,
                tags = tags,
                country = country,
                countryCode = countryCode,
                language = language,
                codec = codec,
                bitrate = bitrate,
                lastCheckOk = lastCheckOk,
                lastCheckTime = lastCheckTime,
                isFavorite = isFavorite
            )
        }
    }
}
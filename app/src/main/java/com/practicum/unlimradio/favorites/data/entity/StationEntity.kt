package com.practicum.unlimradio.favorites.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "station_table",
    indices = [
        Index(
            value = ["stationUuid"],
            unique = true
        )
    ]
)
data class StationEntity(
    @PrimaryKey
    val stationUuid: String,
    val name: String,
    val url: String?,
    val urlResolved: String?,
    val favIcon: String?,
    val tags: String?,
    val country: String?,
    val countryCode: String?,
    val language: String?,
    val codec: String?,
    val bitrate: Int?,
    val lastCheckOk: Int?,
    val lastCheckTime: String?,
    var isFavorite: Boolean
)

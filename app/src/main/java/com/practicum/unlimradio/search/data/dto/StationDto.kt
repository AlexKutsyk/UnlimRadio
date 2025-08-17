package com.practicum.unlimradio.search.data.dto

import com.google.gson.annotations.SerializedName

data class StationDto(
    @SerializedName("stationuuid")
    val stationUuid : String,
    val name: String,
    val url: String? = null,
    @SerializedName("url_resolved")
    val urlResolved: String? = null,
    @SerializedName("favicon")
    val favIcon: String? = null,
    val tags: String? = null,
    val country: String? = null,
    @SerializedName("countrycode")
    val countryCode: String? = null,
    val language: String? = null,
    val codec: String? = null,
    val bitrate: Int? = null,
    @SerializedName("lastcheckok")
    val lastCheckOk: Int? = null,
    @SerializedName("lastchecktime")
    val lastCheckTime: String? = null
)
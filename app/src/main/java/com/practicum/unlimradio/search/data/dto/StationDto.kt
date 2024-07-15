package com.practicum.unlimradio.search.data.dto

import com.google.gson.annotations.SerializedName

data class StationDto(
    val name: String,
    val url: String? = null,
    @SerializedName("url_resolved")
    val urlResolved: String? = null,
    val favicon: String? = null,
    val tags: String? = null,
    val country: String? = null,
    val countrycode: String? = null,
    val language: String? = null,
    val codec: String? = null,
    val bitrate: Int? = null,
    val lastcheckok: Int? = null,
    val lastchecktime: String? = null
)
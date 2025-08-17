package com.practicum.unlimradio.search.domain.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Station(
    val stationUuid: String = "",
    val name: String = "",
    val url: String? = null,
    val urlResolved: String? = null,
    val favicon: String? = null,
    val tags: String? = null,
    val country: String? = null,
    val countryCode: String? = null,
    val language: String? = null,
    val codec: String? = null,
    val bitrate: Int? = null,
    val lastCheckOk: Int? = null,
    val lastCheckTime: String? = null,
    var formatedUrlStation: String = "",
    var isPlaying: Boolean = false,
    var bitmap: Bitmap? = null,
    var isFavorite: Boolean = false
) : Parcelable
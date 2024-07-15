package com.practicum.unlimradio.search.domain.model

data class Station(
    val name: String,
    val url: String? = null,
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

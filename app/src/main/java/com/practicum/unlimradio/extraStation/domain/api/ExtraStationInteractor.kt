package com.practicum.unlimradio.extraStation.domain.api

interface ExtraStationInteractor {
    suspend fun getExtraStation(): Boolean
}
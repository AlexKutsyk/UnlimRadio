package com.practicum.unlimradio.extraStation.domain.api

interface ExtraStationRepository {
    suspend fun getExtraStation(): Boolean
}
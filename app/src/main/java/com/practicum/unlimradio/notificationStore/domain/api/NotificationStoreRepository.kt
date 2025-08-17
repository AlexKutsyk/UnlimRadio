package com.practicum.unlimradio.notificationStore.domain.api

import com.practicum.unlimradio.search.domain.model.Station

interface NotificationStoreRepository {
    fun setLastStation(station: Station)
    fun getLastStation(): Station
}
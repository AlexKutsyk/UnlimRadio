package com.practicum.unlimradio.notificationStore.domain.impl

import com.practicum.unlimradio.notificationStore.domain.api.NotificationStoreInteractor
import com.practicum.unlimradio.notificationStore.domain.api.NotificationStoreRepository
import com.practicum.unlimradio.search.domain.model.Station
import javax.inject.Inject

class NotificationStoreInteractorImpl @Inject constructor(
    private val notificationStoreRepository: NotificationStoreRepository
): NotificationStoreInteractor {

    override fun setLastStation(station: Station) {
        return notificationStoreRepository.setLastStation(station)
    }

    override fun getLastStation(): Station {
        return notificationStoreRepository.getLastStation()
    }
}
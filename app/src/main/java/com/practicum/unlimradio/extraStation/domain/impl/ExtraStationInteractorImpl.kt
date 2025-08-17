package com.practicum.unlimradio.extraStation.domain.impl

import com.practicum.unlimradio.extraStation.domain.api.ExtraStationInteractor
import com.practicum.unlimradio.extraStation.domain.api.ExtraStationRepository
import javax.inject.Inject

class ExtraStationInteractorImpl @Inject constructor(val extraStationRepository: ExtraStationRepository) :
    ExtraStationInteractor {
    override suspend fun getExtraStation(): Boolean {
        return extraStationRepository.getExtraStation()
    }
}
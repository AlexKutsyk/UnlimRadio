package com.practicum.unlimradio.search.domain.api

import com.practicum.unlimradio.search.domain.model.Station
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    suspend fun searchStation(name: String): Flow<Pair<List<Station>?, String?>>
}
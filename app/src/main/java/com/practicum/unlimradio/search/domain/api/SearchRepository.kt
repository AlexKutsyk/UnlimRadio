package com.practicum.unlimradio.search.domain.api

import com.practicum.unlimradio.search.domain.model.Station
import com.practicum.unlimradio.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchStation(name: String): Flow<Resource<List<Station>>>
}
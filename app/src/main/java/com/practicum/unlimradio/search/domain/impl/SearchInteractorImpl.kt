package com.practicum.unlimradio.search.domain.impl

import com.practicum.unlimradio.search.domain.api.SearchInteractor
import com.practicum.unlimradio.search.domain.api.SearchRepository
import com.practicum.unlimradio.search.domain.model.Station
import com.practicum.unlimradio.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchInteractorImpl @Inject constructor(
    private val searchRepository: SearchRepository
) : SearchInteractor {
    override suspend fun searchStation(name: String): Flow<Pair<List<Station>?, String?>> {
        return searchRepository.searchStation(name).map { result ->
            when (result) {

                is Resource.Error -> {
                    Pair(null, result.message)
                }

                is Resource.Success -> {
                    Pair(result.data, null)
                }
            }
        }
    }
}
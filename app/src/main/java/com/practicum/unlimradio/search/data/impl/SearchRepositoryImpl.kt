package com.practicum.unlimradio.search.data.impl

import android.content.Context
import com.practicum.unlimradio.R
import com.practicum.unlimradio.search.data.api.NetworkClient
import com.practicum.unlimradio.search.data.dto.StationSearchRequest
import com.practicum.unlimradio.search.data.dto.StationSearchResponse
import com.practicum.unlimradio.search.domain.api.SearchRepository
import com.practicum.unlimradio.search.domain.model.Station
import com.practicum.unlimradio.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val context: Context
) : SearchRepository {
    override suspend fun searchStation(name: String): Flow<Resource<List<Station>>> = flow {
        val response = networkClient.doRequest(StationSearchRequest(name))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(context.getString(R.string.check_internet_connection)))
            }

            200 -> {
                if ((response as StationSearchResponse).stations.isEmpty()) {
                    emit(Resource.Error(context.getString(R.string.nothing_was_found_for_this_query)))
                } else {
                    emit(
                        Resource.Success(
                            response.stations.map {
                                Station(
                                    name = it.name,
                                    url = it.url,
                                    urlResolved = it.urlResolved,
                                    favicon = it.favicon,
                                    tags = it.tags,
                                    country = it.country,
                                    countrycode = it.countrycode,
                                    language = it.language,
                                    codec = it.codec,
                                    bitrate = it.bitrate,
                                    lastcheckok = it.lastcheckok,
                                    lastchecktime = it.lastchecktime
                                )
                            })
                    )
                }
            }

            else -> {
                emit(Resource.Error(context.getString(R.string.server_error)))
            }
        }
    }

}
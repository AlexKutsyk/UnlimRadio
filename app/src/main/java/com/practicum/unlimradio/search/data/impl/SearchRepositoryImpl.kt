package com.practicum.unlimradio.search.data.impl

import android.content.Context
import com.practicum.unlimradio.R
import com.practicum.unlimradio.favorites.data.AppDatabase
import com.practicum.unlimradio.search.data.api.NetworkClient
import com.practicum.unlimradio.search.data.dto.StationDto
import com.practicum.unlimradio.search.data.dto.StationSearchRequest
import com.practicum.unlimradio.search.data.dto.StationSearchResponse
import com.practicum.unlimradio.search.domain.api.SearchRepository
import com.practicum.unlimradio.search.domain.model.Station
import com.practicum.unlimradio.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val networkClient: NetworkClient,
    private val context: Context,
    private val appDatabase: AppDatabase
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
                                    stationUuid = it.stationUuid,
                                    name = it.name,
                                    url = it.url,
                                    urlResolved = it.urlResolved,
                                    favicon = it.favIcon,
                                    tags = it.tags,
                                    country = it.country,
                                    countryCode = it.countryCode,
                                    language = it.language,
                                    codec = it.codec,
                                    bitrate = it.bitrate,
                                    lastCheckOk = it.lastCheckOk,
                                    lastCheckTime = it.lastCheckTime,
                                    isFavorite = checkIsStationFav(it.stationUuid)
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

    private suspend fun checkIsStationFav(stationUuid: String) : Boolean {
        val favStationUuidList = appDatabase.stationDao().getListUuidFavStation()
        return favStationUuidList.contains(stationUuid)
    }
}
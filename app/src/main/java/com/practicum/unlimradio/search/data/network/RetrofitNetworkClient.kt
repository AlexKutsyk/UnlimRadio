package com.practicum.unlimradio.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.practicum.unlimradio.search.data.api.NetworkClient
import com.practicum.unlimradio.search.data.api.RadioBrowserApiService
import com.practicum.unlimradio.search.data.dto.Response
import com.practicum.unlimradio.search.data.dto.StationDto
import com.practicum.unlimradio.search.data.dto.StationSearchRequest
import com.practicum.unlimradio.search.data.dto.StationSearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RetrofitNetworkClient @Inject constructor (
    private val radioBrowserApiService: RadioBrowserApiService,
    private val context: Context
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {

        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }

        when (dto) {
            is StationSearchRequest -> {
                return withContext(Dispatchers.IO) {
                    try {
                        val response = radioBrowserApiService.getStations(dto.name)
                        val stationSearchResponse = if (response.isSuccessful) {
                            val stations = response.body() as List<StationDto>
                            StationSearchResponse(stations).apply { resultCode = response.code() }
                        } else {
                            errorResponse(response)
                        }
                        stationSearchResponse
                    } catch (e: Throwable) {
                        Log.e("RetrofitNetworkClient", "${e.message}")
                        Log.e("RetrofitNetworkClient", "${e.cause}")
                        Log.e("RetrofitNetworkClient", "${dto.name}")
                        e.printStackTrace()
                        e.fillInStackTrace()
                        Response().apply { resultCode = 500 }
                    }
                }
            }

            else -> {
                return Response().apply { resultCode = 400 }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

    private fun <T> errorResponse(response: retrofit2.Response<T>): Response {
        val code = response.code()
        return Response().apply { resultCode = code }
    }
}
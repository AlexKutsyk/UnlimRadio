package com.practicum.unlimradio.search.data.api;

import com.practicum.unlimradio.search.data.dto.StationDto
import com.practicum.unlimradio.search.data.dto.StationSearchResponse
import com.practicum.unlimradio.utils.Resource
import retrofit2.Response
import retrofit2.http.GET;
import retrofit2.http.Path

interface RadioBrowserApiService {

    @GET("json/stations/byname/{name}")
    suspend fun getStations(@Path("name") name: String): Response<List<StationDto>>
}
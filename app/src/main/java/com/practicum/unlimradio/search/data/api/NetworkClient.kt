package com.practicum.unlimradio.search.data.api

import com.practicum.unlimradio.search.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}
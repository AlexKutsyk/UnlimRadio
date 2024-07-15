package com.practicum.unlimradio.search.data.dto

data class StationSearchResponse(
    val stations: List<StationDto>,
) : Response()

package com.practicum.unlimradio.player.domain.api

interface MediaDataInteractor {
    fun play(mediaUrl: String)
    fun stop()
}
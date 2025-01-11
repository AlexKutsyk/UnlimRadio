package com.practicum.unlimradio.player.domain.api

interface MediaDataSource {
    fun play(mediaUrl: String)
    fun stop()
}
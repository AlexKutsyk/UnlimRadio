package com.practicum.unlimradio.player.domain.impl

import com.practicum.unlimradio.player.domain.api.MediaDataSource
import javax.inject.Inject

class MediaDataInteractorImpl @Inject constructor(
    private val mediaDataSource: MediaDataSource
) : MediaDataSource {
    override fun play(mediaUrl: String) {
        mediaDataSource.play(mediaUrl)
    }

    override fun stop() {
        mediaDataSource.stop()
    }
}
package com.practicum.unlimradio.player.data

import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.practicum.unlimradio.player.domain.api.MediaDataSource
import javax.inject.Inject

class MediaDataSourceImpl @Inject constructor(
    private val exoPlayer: ExoPlayer
) : MediaDataSource {

    override fun play(mediaUrl: String) {
        val mediaItem = MediaItem.fromUri(mediaUrl)
        exoPlayer.addMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    override fun stop() {
        exoPlayer.clearMediaItems()
    }
}
package com.practicum.unlimradio.service

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.practicum.unlimradio.MyApp
import com.practicum.unlimradio.notificationStore.domain.api.NotificationStoreInteractor
import com.practicum.unlimradio.main.ui.MainActivity
import com.practicum.unlimradio.search.domain.model.Station
import javax.inject.Inject

class ExoPlayerService : MediaSessionService() {

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var mediaSession: MediaSession
    private var station: Station? = null

    @Inject
    lateinit var notificationStoreInteractor: NotificationStoreInteractor

    override fun onCreate() {
        super.onCreate()
        (baseContext.applicationContext as MyApp).component.inject(this)
        initExoPlayer()
        initMediaSession(exoPlayer)
    }

    private fun initExoPlayer() {
        exoPlayer = ExoPlayer.Builder(this)
            .build().apply {
                addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        super.onIsPlayingChanged(isPlaying)
                        if (isPlaying) {
                            val stationBundle = exoPlayer.currentMediaItem?.mediaMetadata?.extras
                            station = stationBundle?.let {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    it.getParcelable(STATION_KEY, Station::class.java)
                                } else {
                                    it.getParcelable(STATION_KEY)
                                }
                            }
                            station?.let { notificationStoreInteractor.setLastStation(it) }
                        }
                    }
                })
                setAudioAttributes(
                    AudioAttributes.Builder().setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                        .setUsage(C.USAGE_MEDIA).build(), true
                )
            }
    }

    private fun initMediaSession(exoPlayer: ExoPlayer) {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        mediaSession =
            MediaSession.Builder(this, exoPlayer)
                .setSessionActivity(pendingIntent)
                .build()
    }

    override fun onDestroy() {
        exoPlayer.release()
        mediaSession.release()
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        exoPlayer.release()
        mediaSession.release()
        super.onTaskRemoved(rootIntent)
    }

    companion object {
        const val STATION_KEY = "station"
    }
}
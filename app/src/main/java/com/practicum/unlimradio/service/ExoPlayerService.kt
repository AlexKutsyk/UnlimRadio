package com.practicum.unlimradio.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaStyleNotificationHelper
import com.practicum.unlimradio.R
import com.practicum.unlimradio.main.ui.MainActivity
import com.practicum.unlimradio.search.domain.model.Station

class ExoPlayerService : Service() {

    private val exoPlayer by lazy { ExoPlayer.Builder(baseContext).build() }
    private val mediaSession by lazy { MediaSession.Builder(baseContext, exoPlayer).build() }
    private var actionId = 0

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val station = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent?.getParcelableExtra(STATION_KEY, Station::class.java)
        } else {
            intent?.getParcelableExtra(STATION_KEY)
        }
        Log.i("alex", "onStartCommand/station - $station")
        val urlRadio = station?.formatedUrlStation
        val mediaItem = urlRadio?.let { MediaItem.fromUri(it) }
        mediaItem?.let { exoPlayer.setMediaItem(it) }
        exoPlayer.prepare()
        exoPlayer.play()

        startForeground(1, createNotification(station))
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @OptIn(UnstableApi::class)
    private fun createNotification(station: Station?): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
            .putExtra(STATION_KEY, station)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(station?.name)
            .setSmallIcon(R.drawable.ic_radio)
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(
                R.drawable.ic_play_arrow,
                "Play",
                pendingIntent
            )
            .addAction(
                R.drawable.ic_pause,
                "Pause",
                pendingIntent
            )
            .addAction(
                R.drawable.ic_close,
                "Close",
                pendingIntent
            )
//            .setStyle(MediaStyleNotificationHelper.MediaStyle(mediaSession)
//                .setShowActionsInCompactView(actionId ++))
            .setLargeIcon(station?.bitmap)
            .build()
    }

    companion object {
        const val CHANNEL_ID = "unlim_radio_channel_id"
        const val CHANNEL_NAME = "UnlimRadio"
        const val STATION_KEY = "station"

        fun getIntent(context: Context, station: Station): Intent {
            return Intent(context, ExoPlayerService::class.java)
                .putExtra(STATION_KEY, station)
        }
    }
}
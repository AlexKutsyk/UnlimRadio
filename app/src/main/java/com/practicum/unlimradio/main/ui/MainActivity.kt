package com.practicum.unlimradio.main.ui

import android.content.ComponentName
import android.content.ContentResolver
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.practicum.unlimradio.MyApp
import com.practicum.unlimradio.R
import com.practicum.unlimradio.databinding.ActivityMainBinding
import com.practicum.unlimradio.main.presentation.MainActivityViewModel
import com.practicum.unlimradio.main.presentation.MainActivityViewModelFactory
import com.practicum.unlimradio.search.domain.model.Station
import com.practicum.unlimradio.service.ExoPlayerService
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var station: Station? = null
    private lateinit var mediaController: MediaController
    private lateinit var controllerFuture: ListenableFuture<MediaController>

    @Inject
    lateinit var mainActivityViewModelFactory: MainActivityViewModelFactory
    private val viewModel by viewModels<MainActivityViewModel> { mainActivityViewModelFactory }

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApp).component.inject(this)
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()
        setOnClickListeners()
        viewModel.getFavoriteStations()
    }

    @OptIn(UnstableApi::class)
    override fun onStart() {
        super.onStart()
        val sessionToken = SessionToken(this, ComponentName(this, ExoPlayerService::class.java))
        controllerFuture = MediaController.Builder(this, sessionToken)
            .buildAsync()

        controllerFuture.addListener(
            {
                mediaController = controllerFuture.get()
                if (mediaController.isPlaying) {
                    val stationMetaData = mediaController.mediaMetadata
                    val station = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        stationMetaData.extras?.getParcelable(
                            ExoPlayerService.STATION_KEY,
                            Station::class.java
                        )
                    } else {
                        stationMetaData.extras?.getParcelable(ExoPlayerService.STATION_KEY)
                    }
                    station?.let { showPlayerField(it) }
                }
            },
            MoreExecutors.directExecutor()
        )
    }

    override fun onStop() {
        mediaController.release()
        MediaController.releaseFuture(controllerFuture)
        super.onStop()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun initNavigation() {
        val navView: BottomNavigationView = binding.navView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.navigation_favorites -> {
                    viewModel.getFavoriteStations()
                }
            }
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
            tvOnAir.setOnClickListener {
                station?.isPlaying = false
                if (groupPlayer.isVisible) {
                    groupPlayer.isVisible = false
                    station?.let { stopPlayer(it) }
                }
            }
            ivIconFavOff.setOnClickListener {
                ivIconFavOff.isInvisible = true
                ivIconFavOn.isVisible = true
                station?.let { station -> viewModel.insertStationToFavorite(station) }
            }
            ivIconFavOn.setOnClickListener {
                ivIconFavOff.isVisible = true
                ivIconFavOn.isInvisible = true
                station?.let { station -> viewModel.deleteStationFromFavorite(station) }
            }
        }
    }

    private fun showPlayerField(station: Station) {
        station.isPlaying = true
        with(binding) {
            if (!groupPlayer.isVisible) {
                groupPlayer.isVisible = true
            }
            tvStationName.text = station.name
            with(binding) {
                if (station.isFavorite) {
                    ivIconFavOn.isVisible = true
                    ivIconFavOff.isInvisible = true
                } else {
                    ivIconFavOn.isInvisible = true
                    ivIconFavOff.isVisible = true
                }
            }
            Glide.with(ivStationLogo)
                .load(station.favicon)
                .placeholder(R.drawable.ic_radio)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.i("alex", "onLoadFailed - $isFirstResource")
                        station.copy(favicon = null)
                        Log.i("alex", "$station")

                        val stationBundle = Bundle().apply {
                            putParcelable(ExoPlayerService.STATION_KEY, station)
                        }

                        val iconUri =
                            "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${packageName}/${R.drawable.radio_image}".toUri()

                        Log.i("alex", "iconUri - $iconUri")

                        val mediaMetadata = MediaMetadata.Builder()
                            .setArtist(station.name)
                            .setArtworkUri(iconUri)
                            .setExtras(stationBundle)
                            .setMediaType(MediaMetadata.MEDIA_TYPE_RADIO_STATION)
                            .build()
                        mediaController.setMediaItem(
                            MediaItem.Builder()
                                .setMediaId(station.stationUuid)
                                .setUri(station.formatedUrlStation.toUri())
                                .setMediaMetadata(mediaMetadata)
                                .build()
                        )

                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.i("alex", "onResourceReady - $isFirstResource")
                        Log.i("alex", "onResourceReady/Drawable - $resource")
                        return true
                    }

                })
                .into(ivStationLogo)
        }
    }

    private fun prepareUrlStation(station: Station) {
        if (station.urlResolved.isNullOrEmpty()) {
            Toast.makeText(this, "Отсутствует медиасылка", Toast.LENGTH_SHORT).show()
        } else {

            station.formatedUrlStation = checkSchema(station.urlResolved)
            this.station = station
            Log.i("alex", "newUrlStation - ${station.formatedUrlStation}")
        }
    }

    private fun checkSchema(urlStation: String): String {
        val schema = urlStation.substringBefore("://")
        return if (schema == "http") {
            urlStation.replace("http", "https", true)
        } else {
            urlStation
        }
    }

    fun startPlayer(station: Station) {
        showPlayerField(station)
        stopPlayer(station)
        prepareUrlStation(station)

        val stationBundle = Bundle().apply {
            putParcelable(ExoPlayerService.STATION_KEY, station)
        }

        val iconUri = if (station.favicon.isNullOrEmpty()) {
            "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${packageName}/${R.drawable.radio_image}".toUri()
//            FileProvider.getUriForFile(
//                this,
//                "${packageName}.fileprovider",
//                File("${filesDir}/drawable/radio_image.png") // Путь к вашему изображению
//            )
        } else {
            station.favicon.toUri()
        }

        Log.i("alex", "iconUri - $iconUri")

        val mediaMetadata = MediaMetadata.Builder()
            .setArtist(station.name)
            .setArtworkUri(iconUri)
            .setExtras(stationBundle)
            .setMediaType(MediaMetadata.MEDIA_TYPE_RADIO_STATION)
            .build()
        mediaController.setMediaItem(
            MediaItem.Builder()
                .setUri(station.formatedUrlStation.toUri())
                .setMediaMetadata(mediaMetadata)
                .build()
        )
        mediaController.prepare()
        mediaController.play()
    }

    private fun stopPlayer(station: Station) {
        mediaController.stop()
    }
}
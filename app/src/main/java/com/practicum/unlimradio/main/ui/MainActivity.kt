package com.practicum.unlimradio.main.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.practicum.unlimradio.MyApp
import com.practicum.unlimradio.R
import com.practicum.unlimradio.databinding.ActivityMainBinding
import com.practicum.unlimradio.main.presentation.MainActivityViewModel
import com.practicum.unlimradio.main.presentation.MainActivityViewModelFactory
import com.practicum.unlimradio.search.domain.model.Station
import com.practicum.unlimradio.service.ExoPlayerService
import com.practicum.unlimradio.service.ExoPlayerService.Companion.STATION_KEY
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var station: Station? = null

    @Inject
    lateinit var mainActivityViewModelFactory: MainActivityViewModelFactory
    private val viewModel by viewModels<MainActivityViewModel> { mainActivityViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApp).component.inject(this)
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkIntent()
        initNavigation()
        setOnClickListeners()
        viewModel.getFavoriteStations()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun checkIntent() {
        val intent = intent
        if (intent != null && intent.hasExtra(STATION_KEY)) {
            station = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(STATION_KEY, Station::class.java)
            } else {
                intent.getParcelableExtra(STATION_KEY)
            }
            station?.let { showPlayerField(it) }
        }
    }

    private fun initNavigation() {
        val navView: BottomNavigationView = binding.navView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)
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
                .into(ivStationLogo)
        }
    }

    private fun prepareUrlStation(station: Station) {
        if (station.urlResolved.isNullOrEmpty()) {
            Toast.makeText(this, "Отсутствует медиасылка", Toast.LENGTH_SHORT).show()
        } else {
            if (checkUrl(station.urlResolved)) {
                Toast.makeText(
                    this, "Не могу прочитать файл", Toast.LENGTH_SHORT
                ).show()
//                return
            }
            station.formatedUrlStation = checkSchema(station.urlResolved)
            this.station = station
            Log.i("alex", "newUrlStation - ${station.formatedUrlStation}")
        }
    }

    private fun checkUrl(urlResolved: String): Boolean {
        val typeFile = urlResolved.substringAfterLast('.')
        val incorrectTypeFile = listOf("m3u8")
        return incorrectTypeFile.contains(typeFile)
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
        getBitmapFromIcon(station)
    }

    private fun stopPlayer(station: Station) {
        stopService(ExoPlayerService.getIntent(this, station))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getBitmapFromIcon(station: Station) {
        try {
            Glide.with(this)
                .asBitmap()
                .load(station.favicon)
                .placeholder(R.drawable.ic_radio)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
//                        station.bitmap = resource
//                        Log.i("alex", "onResourceReady - $resource")
//                        startForegroundService(
//                            ExoPlayerService.getIntent(
//                                this@MainActivity,
//                                station
//                            )
//                        )
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        Log.i("alex", "onLoadCleared")
                    }
                })
        } catch (e: InterruptedException) {
            Log.i("alex", "catch - ${e.message}")
            station.bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_radio)
            startForegroundService(ExoPlayerService.getIntent(this@MainActivity, station))
        } finally {
            station.bitmap = getDrawable(R.drawable.ic_radio)?.toBitmap()
            Log.i("alex", "finally - ${station.bitmap}")
            startForegroundService(ExoPlayerService.getIntent(this@MainActivity, station))
        }
    }
}
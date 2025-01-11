package com.practicum.unlimradio.favorites.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.practicum.unlimradio.R
import com.practicum.unlimradio.databinding.FragmentFavoritesBinding
import com.practicum.unlimradio.main.presentation.MainActivityViewModel
import com.practicum.unlimradio.main.ui.MainActivity
import com.practicum.unlimradio.search.domain.model.Station
import com.practicum.unlimradio.search.presentation.models.ScreenState
import com.practicum.unlimradio.search.ui.SearchAdapter
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

//    @Inject
//    lateinit var factory: FavoritesViewModelFactory
//    val viewModel by viewModels<FavoritesViewModel> { factory }

    private val mainActivityViewModel by activityViewModels<MainActivityViewModel>()

    private val adapterStations = SearchAdapter(object : SearchAdapter.StationClickListener {
        override fun onStationClick(station: Station) {
            (requireActivity() as MainActivity).startPlayer(station)
            val urlStation: String? = station.urlResolved
            urlStation?.let { Log.i("alex", "Favorite - $station") }
        }
    })

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        (context.applicationContext as MyApp).component.inject(this)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fsRvStationList.adapter = adapterStations
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainActivityViewModel.favoriteStationListState.collect { stationScreen ->

                    when (stationScreen) {
                        is ScreenState.Content -> renderContent(stationScreen.stations)
                        ScreenState.Empty -> renderEmpty()
                        is ScreenState.Error -> renderError(stationScreen.message)
                        ScreenState.Loading -> renderProgress()
                        ScreenState.Default -> renderEmpty()
                    }
                }
            }
        }
    }

    private fun renderProgress() {
        with(binding) {
            ivImageSearch.isVisible = false
            tvMessage.isVisible = false
            progressCircular.isVisible = true
            fsRvStationList.isVisible = false
        }
    }

    private fun renderError(message: String) {
        with(binding) {
            tvMessage.text = message
            ivImageSearch.setImageResource(R.drawable.ic_search_off)
            ivImageSearch.isVisible = true
            tvMessage.isVisible = true
            progressCircular.isVisible = false
            fsRvStationList.isVisible = false
        }
    }

    private fun renderEmpty() {
        with(binding) {
            tvMessage.text = getText(R.string.add_favorite_stations)
            ivImageSearch.setImageResource(R.drawable.ic_library_music)
            ivImageSearch.isVisible = true
            tvMessage.isVisible = true
            progressCircular.isVisible = false
            fsRvStationList.isVisible = false
        }
    }

    private fun renderContent(stations: List<Station>) {
        adapterStations.stations = stations
        adapterStations.notifyDataSetChanged()
        with(binding) {
            ivImageSearch.isVisible = false
            tvMessage.isVisible = false
            progressCircular.isVisible = false
            fsRvStationList.isVisible = true
        }
    }
}
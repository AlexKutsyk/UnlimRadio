package com.practicum.unlimradio.search.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.practicum.unlimradio.R
import com.practicum.unlimradio.databinding.FragmentSearchBinding
import com.practicum.unlimradio.search.domain.model.Station
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SearchViewModel>()

    var editTextSearch = ""
    private val mediaPlayer = MediaPlayer()
    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(binding.bottomSheet) }
    private var urlStation: String? = null

    private val botNav: BottomNavigationView by lazy { requireActivity().findViewById(R.id.nav_view) }

    private val adapterStations = SearchAdapter(
        object : SearchAdapter.StationClickListener {
            override fun onStationClick(station: Station) {
                binding.onAir.isVisible = false
                bottomSheetBehavior.isHideable = true
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                mediaPlayer.reset()
//                binding.bsNameStation.text = requireContext().getString(R.string.empty_text)
//                binding.onAir.isVisible = false
                urlStation = station.urlResolved
//                Snackbar.make(
//                    requireContext(),
//                    requireView(),
//                    station.url as CharSequence,
//                    Snackbar.LENGTH_SHORT
//                ).show()
                prepareMediaPlayer(station)
            }
        }
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputSearch()
        setOnClickListener()

        binding.fsRvStationList.adapter = adapterStations

        viewModel.getUiState().observe(viewLifecycleOwner) { uiState ->
            adapterStations.stations = uiState
            adapterStations.notifyDataSetChanged()
        }

        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        botNav.isVisible = false
                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        botNav.isVisible = true
                    }

                    else -> {
                        // Остальные состояния не обрабатываем
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    private fun prepareMediaPlayer(station: Station) {
        if (urlStation.isNullOrEmpty()) {
            mediaPlayer.release()
        } else {
            mediaPlayer.setDataSource(urlStation)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                with(binding) {
                    bsNameStation.text = station.name
                }
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                botNav.isVisible = false
            }
            mediaPlayer.setOnCompletionListener {
                binding.onAir.isVisible = false
            }
        }
    }

    private fun setOnClickListener() {
        with(binding) {
            fsEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.searchStation(editTextSearch)
                    fsRvStationList.smoothScrollToPosition(0)
                }
                true
            }

            fsIvIconClear.setOnClickListener {
                fsEditText.setText(getString(R.string.empty_text))
            }
            buttonPlay.setOnClickListener {
                onAir.isVisible = true
                mediaPlayer.start()
                bottomSheetBehavior.isHideable = false
            }
            buttonStop.setOnClickListener {
                mediaPlayer.pause()
                onAir.isVisible = false
                bottomSheetBehavior.isHideable = true
            }
        }

    }

    private fun inputSearch() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editTextSearch = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        binding.fsEditText.addTextChangedListener(textWatcher)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
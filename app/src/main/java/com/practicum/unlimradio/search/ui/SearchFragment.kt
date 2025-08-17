package com.practicum.unlimradio.search.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.practicum.unlimradio.main.ui.MainActivity
import com.practicum.unlimradio.MyApp
import com.practicum.unlimradio.R
import com.practicum.unlimradio.databinding.FragmentSearchBinding
import com.practicum.unlimradio.search.presentation.ViewModelFactory
import com.practicum.unlimradio.search.domain.model.Station
import com.practicum.unlimradio.search.presentation.SearchViewModel
import com.practicum.unlimradio.search.presentation.models.ScreenState
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<SearchViewModel> { viewModelFactory }
    private val component by lazy {
        (requireActivity().application as MyApp).component
    }
    var editTextSearch = ""
    private val adapterStations = SearchAdapter(object : SearchAdapter.StationClickListener {
        override fun onStationClick(station: Station) {
            (requireActivity() as MainActivity).startPlayer(station)
            val urlStation: String? = station.urlResolved
            urlStation?.let { Log.i("alex", it) }
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        component.inject(this)
        super.onViewCreated(view, savedInstanceState)
        binding.fsRvStationList.adapter = adapterStations
        inputSearch()
        setOnClickListener()
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClickListener() {
        with(binding) {
            fsEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH && checkLengthEditText(editTextSearch)) {
                    hideKeyboard(this@SearchFragment)
                    viewModel.searchStation(editTextSearch)
                    fsRvStationList.smoothScrollToPosition(0)
                }
                true
            }
            fsIvIconClear.setOnClickListener {
                fsEditText.setText(getString(R.string.empty_text))
                viewModel.getSecretStation()
            }
            fsIvIconSearch.setOnClickListener {
                if (checkLengthEditText(editTextSearch)) {
                    hideKeyboard(this@SearchFragment)
                    viewModel.searchStation(editTextSearch)
                    fsRvStationList.smoothScrollToPosition(0)
                }
            }
            fsIvIconSettings.setOnClickListener { }
        }
    }

    private fun inputSearch() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) = Unit

            override fun onTextChanged(
                s: CharSequence?, start: Int, before: Int, count: Int
            ) {
                editTextSearch = s.toString()
            }

            override fun afterTextChanged(s: Editable?) = Unit
        }
        binding.fsEditText.addTextChangedListener(textWatcher)
    }

    private fun hideKeyboard(fragment: Fragment) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = fragment.view?.findViewById<EditText>(R.id.fs_edit_text)
        view?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is ScreenState.Content -> renderContent(uiState.stations)
                        ScreenState.Default -> renderDefault()
                        ScreenState.Empty -> renderEmpty()
                        is ScreenState.Error -> renderError(uiState.message)
                        ScreenState.Loading -> renderProgress()
                    }

                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.extraStationState.collect { extraStationState ->
                    if (extraStationState) {
                        Toast
                            .makeText(
                                context,
                                getString(R.string.station_added),
                                Toast.LENGTH_SHORT
                            )
                            .show()
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
            tvMessage.text = getText(R.string.nothing_was_found_for_this_query)
            ivImageSearch.setImageResource(R.drawable.ic_search_off)
            ivImageSearch.isVisible = true
            tvMessage.isVisible = true
            progressCircular.isVisible = false
            fsRvStationList.isVisible = false
        }
    }

    private fun renderDefault() {
        with(binding) {
            tvMessage.text = getText(R.string.station_search)
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

    private fun checkLengthEditText(text: String): Boolean {
        return if (text.trim().length < 3) {
            Toast.makeText(
                context,
                getString(R.string.query_length_must_be_more_than_2_letters),
                Toast.LENGTH_SHORT
            ).show()
            false
        } else {
            true
        }
    }
}
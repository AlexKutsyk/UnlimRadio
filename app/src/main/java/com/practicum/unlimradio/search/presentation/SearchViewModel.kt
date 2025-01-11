package com.practicum.unlimradio.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.unlimradio.search.domain.api.SearchInteractor
import com.practicum.unlimradio.search.domain.model.Station
import com.practicum.unlimradio.search.presentation.models.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchInteractor: SearchInteractor
) : ViewModel() {
    private var _uiState = MutableStateFlow<ScreenState>(ScreenState.Default)
    val uiState = _uiState.asStateFlow()

    fun searchStation(name: String) {
        _uiState.value = ScreenState.Loading
        viewModelScope.launch {
            searchInteractor.searchStation(name).first { result ->
                handleSearch(result)
                true
            }
        }
    }

    private fun handleSearch(resultSearch: Pair<List<Station>?, String?>) {
        val listStation = resultSearch.first
        val message = resultSearch.second
        if (message != null) {
            _uiState.value = ScreenState.Error(message)
        } else {
            listStation?.let { stations ->
                if (stations.isEmpty()) {
                    _uiState.value = ScreenState.Empty
                } else {
                    _uiState.value = ScreenState.Content(stations)
                }
            }
        }
    }
}
package com.practicum.unlimradio.search.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.unlimradio.search.domain.api.SearchInteractor
import com.practicum.unlimradio.search.domain.model.Station
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private var _uiState = MutableLiveData<List<Station>>()
    fun getUiState(): LiveData<List<Station>> = _uiState

    fun searchStation(name: String) {
        Log.i("alex", "Ищем станции _${name}_")
        viewModelScope.launch {
            searchInteractor.searchStation(name).collect { result ->
                if (result.first != null) {
                    if (result.first!!.isEmpty()) {
                        Log.i("alex", "Ничего не найдено")
                    } else {
                        _uiState.postValue(result.first)
                        Log.i("alex", "${result.first}")
                    }
                }
                if (result.second != null) {
                    Log.i("alex", "Ошибка - ${result.second}")
                }
            }
        }
    }

}
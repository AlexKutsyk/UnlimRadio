package com.practicum.unlimradio.notificationStore.data.impl

import android.content.SharedPreferences
import android.util.Log
import com.practicum.unlimradio.notificationStore.domain.api.NotificationStoreRepository
import javax.inject.Inject
import androidx.core.content.edit
import com.google.gson.Gson
import com.practicum.unlimradio.search.domain.model.Station

class NotificationStoreRepositoryImpl @Inject constructor(
    private val sharedPref: SharedPreferences
) : NotificationStoreRepository {

    override fun setLastStation(station: Station) {
        Log.i("alex", "setLastStation - $station")
        sharedPref.edit {
            val stationJson = Gson().toJson(station)
            putString(STATION_KEY, stationJson)
        }
    }

    override fun getLastStation(): Station {
        val stationJson = sharedPref.getString(STATION_KEY, "") ?: ""
        return Gson().fromJson(stationJson, Station::class.java)
    }

    companion object {
        private const val STATION_KEY = "station_key"
    }
}
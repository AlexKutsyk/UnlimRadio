package com.practicum.unlimradio.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.unlimradio.R
import com.practicum.unlimradio.search.domain.model.Station

class SearchAdapter(
    private val clickListener: StationClickListener
) : RecyclerView.Adapter<SearchViewHolder>() {

    var stations: List<Station> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_station_layout,parent,false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stations.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(stations[position])
        holder.itemView.setOnClickListener { clickListener.onStationClick(stations[position]) }
    }

    interface StationClickListener{
        fun onStationClick(station: Station)
    }
}
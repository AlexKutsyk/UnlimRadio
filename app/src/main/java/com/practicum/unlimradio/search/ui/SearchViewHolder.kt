package com.practicum.unlimradio.search.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.unlimradio.R
import com.practicum.unlimradio.search.domain.model.Station

class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val iconStation: ImageView = itemView.findViewById(R.id.logo_station)
    private val nameStation: TextView = itemView.findViewById(R.id.name_station)
    private val tagStation: TextView = itemView.findViewById(R.id.tv_tags)
    private val codecStation: TextView = itemView.findViewById(R.id.tv_codec)
    private val bitrateStation: TextView = itemView.findViewById(R.id.tv_bitrate)
    private val countryStation: TextView = itemView.findViewById(R.id.tv_country_code)
    private val languageStation: TextView = itemView.findViewById(R.id.tv_language)

    fun bind(model: Station) {
        Glide.with(iconStation)
            .load(model.favicon)
            .centerCrop()
            .transform(RoundedCorners(12))
            .placeholder(R.drawable.ic_radio)
            .into(iconStation)

        with(model) {
            nameStation.text = name
            tagStation.text = tags
            codecStation.text = codec
            bitrateStation.text = buildString {
                append(bitrate.toString())
                append(" kbps")
            }
            countryStation.text = countryCode
            languageStation.text = language
        }
    }
}
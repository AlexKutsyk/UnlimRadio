package com.practicum.unlimradio.di

import com.practicum.unlimradio.search.data.api.NetworkClient
import com.practicum.unlimradio.search.data.api.RadioBrowserApiService
import com.practicum.unlimradio.search.data.impl.SearchRepositoryImpl
import com.practicum.unlimradio.search.data.network.RetrofitNetworkClient
import com.practicum.unlimradio.search.domain.api.SearchRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://at1.api.radio-browser.info/"

val dataModule = module {

    single<RadioBrowserApiService> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RadioBrowserApiService::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(
            get(),
            androidContext()
        )
    }

    single<SearchRepository> {
        SearchRepositoryImpl(
            get(),
            androidContext()
        )
    }
}
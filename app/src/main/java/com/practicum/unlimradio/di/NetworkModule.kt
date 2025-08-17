package com.practicum.unlimradio.di

import com.practicum.unlimradio.search.data.api.RadioBrowserApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule() {
    @Provides
    fun provideRadioBrowserApiService(): RadioBrowserApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RadioBrowserApiService::class.java)
    }

    companion object {
        const val BASE_URL = "https://de1.api.radio-browser.info/"
    }
}
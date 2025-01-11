package com.practicum.unlimradio.di

import com.practicum.unlimradio.favorites.data.FavoritesRepositoryImpl
import com.practicum.unlimradio.favorites.domain.api.FavoritesRepository
import com.practicum.unlimradio.search.data.api.NetworkClient
import com.practicum.unlimradio.search.data.impl.SearchRepositoryImpl
import com.practicum.unlimradio.search.data.network.RetrofitNetworkClient
import com.practicum.unlimradio.search.domain.api.SearchRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(
    includes = [
        NetworkModule::class,
        DatabaseModule::class
    ]
)
interface DataModule {

    @Singleton
    @Binds
    fun bindRetrofitNetworkClient(retrofitNetworkClient: RetrofitNetworkClient): NetworkClient

    @Singleton
    @Binds
    fun bindSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository

    @Singleton
    @Binds
    fun bindFavoritesRepository(favoritesRepositoryImpl: FavoritesRepositoryImpl): FavoritesRepository
}
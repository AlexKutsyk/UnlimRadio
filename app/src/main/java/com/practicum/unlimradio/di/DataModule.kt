package com.practicum.unlimradio.di

import com.practicum.unlimradio.notificationStore.data.impl.NotificationStoreRepositoryImpl
import com.practicum.unlimradio.notificationStore.domain.api.NotificationStoreRepository
import com.practicum.unlimradio.favorites.data.FavoritesRepositoryImpl
import com.practicum.unlimradio.favorites.domain.api.FavoritesRepository
import com.practicum.unlimradio.search.data.api.NetworkClient
import com.practicum.unlimradio.search.data.impl.SearchRepositoryImpl
import com.practicum.unlimradio.search.data.network.RetrofitNetworkClient
import com.practicum.unlimradio.search.domain.api.SearchRepository
import com.practicum.unlimradio.extraStation.data.ExtraStationRepositoryImpl
import com.practicum.unlimradio.extraStation.domain.api.ExtraStationRepository
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

    @Singleton
    @Binds
    fun bindSecretFeatureRepository(extraStationRepositoryImpl: ExtraStationRepositoryImpl): ExtraStationRepository

    @Singleton
    @Binds
    fun bindDataStoreRepository(dataStoreRepositoryImpl: NotificationStoreRepositoryImpl): NotificationStoreRepository
}
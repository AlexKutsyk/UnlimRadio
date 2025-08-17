package com.practicum.unlimradio.di

import com.practicum.unlimradio.notificationStore.domain.api.NotificationStoreInteractor
import com.practicum.unlimradio.notificationStore.domain.impl.NotificationStoreInteractorImpl
import com.practicum.unlimradio.favorites.domain.api.FavoritesInteractor
import com.practicum.unlimradio.favorites.domain.impl.FavoritesInteractorImpl
import com.practicum.unlimradio.search.domain.api.SearchInteractor
import com.practicum.unlimradio.search.domain.impl.SearchInteractorImpl
import com.practicum.unlimradio.extraStation.domain.api.ExtraStationInteractor
import com.practicum.unlimradio.extraStation.domain.impl.ExtraStationInteractorImpl
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @Binds
    fun bindSearchInteractor(searchInteractorImpl: SearchInteractorImpl): SearchInteractor

    @Binds
    fun bindFavoriteInteractor(favoritesInteractorImpl: FavoritesInteractorImpl): FavoritesInteractor

    @Binds
    fun bindSecretFeatureInteractor(secretFeatureInteractorImpl: ExtraStationInteractorImpl): ExtraStationInteractor

    @Binds
    fun bindDataStoreInteractor(dataStoreInteractorImpl: NotificationStoreInteractorImpl): NotificationStoreInteractor
}
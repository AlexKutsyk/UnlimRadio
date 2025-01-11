package com.practicum.unlimradio.di

import com.practicum.unlimradio.favorites.domain.api.FavoritesInteractor
import com.practicum.unlimradio.favorites.domain.impl.FavoritesInteractorImpl
import com.practicum.unlimradio.search.domain.api.SearchInteractor
import com.practicum.unlimradio.search.domain.impl.SearchInteractorImpl
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @Binds
    fun bindSearchInteractor(searchInteractorImpl: SearchInteractorImpl): SearchInteractor

    @Binds
    fun bindFavoriteInteractor(favoritesInteractorImpl: FavoritesInteractorImpl): FavoritesInteractor
}
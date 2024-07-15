package com.practicum.unlimradio.di

import com.practicum.unlimradio.search.domain.api.SearchInteractor
import com.practicum.unlimradio.search.domain.impl.SearchInteractorImpl
import org.koin.dsl.module

val domainModule = module {
    factory <SearchInteractor> {
        SearchInteractorImpl(get())
    }
}
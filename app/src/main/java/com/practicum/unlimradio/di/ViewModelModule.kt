package com.practicum.unlimradio.di

import androidx.lifecycle.ViewModel
import com.practicum.unlimradio.search.presentation.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    @Binds
    fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel
}
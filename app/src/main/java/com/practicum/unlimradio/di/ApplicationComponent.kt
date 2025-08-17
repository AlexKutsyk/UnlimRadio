package com.practicum.unlimradio.di

import com.practicum.unlimradio.favorites.ui.FavoritesFragment
import com.practicum.unlimradio.main.ui.MainActivity
import com.practicum.unlimradio.search.ui.SearchFragment
import com.practicum.unlimradio.service.ExoPlayerService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DataModule::class,
        DomainModule::class,
        ViewModelModule::class,
        ContextModule::class,
        NotificationStoreModule::class
    ]
)
interface ApplicationComponent {
    fun inject(fragment: SearchFragment)
    fun inject(activity: MainActivity)
    fun inject(fragment: FavoritesFragment)
    fun inject(service: ExoPlayerService)
}
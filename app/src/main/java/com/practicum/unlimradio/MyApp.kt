package com.practicum.unlimradio

import android.app.Application
import com.practicum.unlimradio.di.dataModule
import com.practicum.unlimradio.di.domainModule
import com.practicum.unlimradio.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(
                dataModule,
                domainModule,
                viewModelModule
            )
        }
    }
}
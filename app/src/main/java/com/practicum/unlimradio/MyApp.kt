package com.practicum.unlimradio

import android.app.Application
import com.practicum.unlimradio.di.ApplicationComponent
import com.practicum.unlimradio.di.ContextModule
import com.practicum.unlimradio.di.DaggerApplicationComponent

class MyApp : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
            .contextModule(ContextModule(this))
            .build()
    }
}
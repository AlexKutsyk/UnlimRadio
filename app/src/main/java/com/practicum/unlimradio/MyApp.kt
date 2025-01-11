package com.practicum.unlimradio

import android.app.Application
import com.practicum.unlimradio.di.ContextModule
import com.practicum.unlimradio.di.DaggerApplicationComponent
import com.practicum.unlimradio.di.DatabaseModule

class MyApp : Application() {

    val component by lazy {
        DaggerApplicationComponent.builder()
            .contextModule(ContextModule(this))
//            .databaseModule(DatabaseModule(this))
            .build()
    }
}
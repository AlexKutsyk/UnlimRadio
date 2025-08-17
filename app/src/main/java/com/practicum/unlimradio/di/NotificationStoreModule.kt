package com.practicum.unlimradio.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NotificationStoreModule {

    @Singleton
    @Provides
    fun provideNotificationSore(context: Context): SharedPreferences {
        return context.getSharedPreferences("unlim_radio.preferences", Context.MODE_PRIVATE)
    }
}
package com.practicum.unlimradio.di

import android.content.Context
import androidx.room.Room
import com.practicum.unlimradio.favorites.data.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "database.db").build()
    }
}
package com.practicum.unlimradio.favorites.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.unlimradio.favorites.data.dao.StationDao
import com.practicum.unlimradio.favorites.data.entity.StationEntity
import javax.inject.Inject

@Database(version = 1, entities = [StationEntity::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun stationDao(): StationDao
}
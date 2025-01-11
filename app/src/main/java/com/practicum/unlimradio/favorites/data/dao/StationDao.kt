package com.practicum.unlimradio.favorites.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.unlimradio.favorites.data.entity.StationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StationDao {

    @Insert(entity = StationEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStationToFav(stationEntity: StationEntity): Long

    @Query("SELECT * FROM station_table")
    suspend fun getFavStations(): List<StationEntity>

    @Query("DELETE FROM station_table WHERE stationUuid LIKE :stationUuid")
    suspend fun deleteStationFromFavById(stationUuid: String): Int

    @Query("SELECT stationUuid FROM station_table")
    suspend fun getListUuidFavStation() : List<String>
}
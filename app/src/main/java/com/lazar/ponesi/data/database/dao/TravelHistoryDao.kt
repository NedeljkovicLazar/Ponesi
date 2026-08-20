package com.lazar.ponesi.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lazar.ponesi.data.database.entity.TravelHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TravelHistoryDao {

    @Query(
        """
        SELECT * FROM travel_history
        ORDER BY endDate DESC, id DESC
        """
    )
    fun getAllHistory(): Flow<List<TravelHistoryEntity>>

    @Query("SELECT * FROM travel_history WHERE id = :historyId")
    fun getHistory(historyId: Int): Flow<TravelHistoryEntity?>

    @Insert
    suspend fun insertHistory(
        history: TravelHistoryEntity
    ): Long

    @Update
    suspend fun updateHistory(
        history: TravelHistoryEntity
    )

    @Delete
    suspend fun deleteHistory(
        history: TravelHistoryEntity
    )
}
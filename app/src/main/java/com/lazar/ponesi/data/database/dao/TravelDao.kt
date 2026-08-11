package com.lazar.ponesi.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.lazar.ponesi.data.database.entity.CategoryEntity
import com.lazar.ponesi.data.database.entity.PackingItemEntity
import com.lazar.ponesi.data.database.entity.TravelEntity
import com.lazar.ponesi.data.database.relation.TravelWithCategories
import kotlinx.coroutines.flow.Flow

@Dao
interface TravelDao {

    @Insert
    suspend fun insertTravel(
        travel: TravelEntity
    ): Long

    @Insert
    suspend fun insertCategory(
        category: CategoryEntity
    ): Long

    @Insert
    suspend fun insertPackingItem(
        item: PackingItemEntity
    ): Long

    @Update
    suspend fun updateTravel(
        travel: TravelEntity
    )

    @Query("DELETE FROM categories WHERE travelId = :travelId")
    suspend fun deleteCategoriesForTravel(
        travelId: Int
    )

    @Transaction
    @Query("SELECT * FROM travels ORDER BY id DESC")
    fun getAllTravelsWithCategories(): Flow<List<TravelWithCategories>>

    @Transaction
    @Query("SELECT * FROM travels WHERE id = :travelId")
    suspend fun getTravelWithCategories(
        travelId: Int
    ): TravelWithCategories?

    @Delete
    suspend fun deleteTravel(
        travel: TravelEntity
    )
}
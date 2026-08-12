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
import com.lazar.ponesi.data.model.TravelStatus
import java.time.LocalDate
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

    @Query(
        """
        UPDATE packing_items
        SET isChecked = :isChecked
        WHERE id = :itemId
        """
    )
    suspend fun updatePackingItemChecked(
        itemId: Int,
        isChecked: Boolean
    )

    @Query(
        """
        UPDATE packing_items
        SET isChecked = :isChecked
        WHERE categoryId = :categoryId
        """
    )
    suspend fun updateCategoryItemsChecked(
        categoryId: Int,
        isChecked: Boolean
    )

    @Query(
        """
    UPDATE travels
    SET status = :status,
        date = :date
    WHERE id = :travelId
    """
    )
    suspend fun updateTravelStatusAndDate(
        travelId: Int,
        status: TravelStatus,
        date: LocalDate?
    )

    @Query(
        """
    UPDATE packing_items
    SET isChecked = 0
    WHERE categoryId IN (
        SELECT id
        FROM categories
        WHERE travelId = :travelId
    )
    """
    )
    suspend fun uncheckAllItemsForTravel(
        travelId: Int
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
package com.lazar.ponesi.data.repository

import androidx.room.withTransaction
import com.lazar.ponesi.data.database.AppDatabase
import com.lazar.ponesi.data.database.entity.CategoryEntity
import com.lazar.ponesi.data.database.entity.PackingItemEntity
import com.lazar.ponesi.data.database.entity.TravelEntity
import com.lazar.ponesi.data.database.entity.TravelHistoryEntity
import com.lazar.ponesi.data.mapper.toTravel
import com.lazar.ponesi.data.model.Category
import com.lazar.ponesi.data.model.Travel
import com.lazar.ponesi.data.model.TravelLocation
import com.lazar.ponesi.data.model.TravelStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class TravelRepository(
    private val database: AppDatabase
) {

    private val travelDao = database.travelDao()

    fun getAllTravels(): Flow<List<Travel>> {
        return travelDao
            .getAllTravelsWithCategories()
            .map { travels ->
                travels.map { travelWithCategories ->
                    travelWithCategories.toTravel()
                }
            }
    }

    suspend fun getTravel(travelId: Int): Travel? {
        return travelDao
            .getTravelWithCategories(travelId)
            ?.toTravel()
    }

    suspend fun saveTravel(
        name: String,
        categories: List<Category>
    ) {
        database.withTransaction {

            val travelId = travelDao.insertTravel(
                TravelEntity(
                    name = name,
                    status = TravelStatus.INACTIVE
                )
            ).toInt()

            categories.forEachIndexed { categoryPosition, category ->

                val categoryId = travelDao.insertCategory(
                    CategoryEntity(
                        travelId = travelId,
                        name = category.name,
                        position = categoryPosition
                    )
                ).toInt()

                category.items.forEachIndexed { itemPosition, item ->

                    travelDao.insertPackingItem(
                        PackingItemEntity(
                            categoryId = categoryId,
                            name = item.name,
                            isChecked = item.isChecked,
                            position = itemPosition
                        )
                    )
                }
            }
        }
    }

    suspend fun updateTravel(
        travelId: Int,
        name: String,
        categories: List<Category>
    ) {
        database.withTransaction {

            val existingTravel = travelDao.getTravelWithCategories(travelId)
                ?: return@withTransaction

            travelDao.updateTravel(
                existingTravel.travel.copy(
                    name = name
                )
            )

            travelDao.deleteCategoriesForTravel(travelId)

            categories.forEachIndexed { categoryPosition, category ->

                val categoryId = travelDao.insertCategory(
                    CategoryEntity(
                        travelId = travelId,
                        name = category.name,
                        position = categoryPosition
                    )
                ).toInt()

                category.items.forEachIndexed { itemPosition, item ->

                    travelDao.insertPackingItem(
                        PackingItemEntity(
                            categoryId = categoryId,
                            name = item.name,
                            isChecked = item.isChecked,
                            position = itemPosition
                        )
                    )
                }
            }
        }
    }

    suspend fun deleteTravel(travelId: Int) {

        val existingTravel = travelDao
            .getTravelWithCategories(travelId)
            ?.travel
            ?: return

        travelDao.deleteTravel(existingTravel)
    }

    suspend fun updatePackingItemChecked(
        itemId: Int,
        isChecked: Boolean
    ) {
        travelDao.updatePackingItemChecked(
            itemId = itemId,
            isChecked = isChecked
        )
    }

    suspend fun updateCategoryItemsChecked(
        categoryId: Int,
        isChecked: Boolean
    ) {
        travelDao.updateCategoryItemsChecked(
            categoryId = categoryId,
            isChecked = isChecked
        )
    }

    suspend fun scheduleTravel(
        travelId: Int,
        date: LocalDate
    ) {
        val existingTravel = travelDao
            .getTravelWithCategories(travelId)
            ?.travel
            ?: return

        travelDao.updateTravel(
            existingTravel.copy(
                status = TravelStatus.SCHEDULED,
                date = date,
                startDate = null
            )
        )
    }

    suspend fun cancelScheduledTravel(
        travelId: Int
    ) {
        val existingTravel = travelDao
            .getTravelWithCategories(travelId)
            ?.travel
            ?: return

        travelDao.updateTravel(
            existingTravel.copy(
                status = TravelStatus.INACTIVE,
                date = null,
                startDate = null,
                locationName = null,
                locationLatitude = null,
                locationLongitude = null
            )
        )
    }

    suspend fun updateTravelLocation(
        travelId: Int,
        location: TravelLocation?
    ) {
        val existingTravel = travelDao
            .getTravelWithCategories(travelId)
            ?.travel
            ?: return

        if (existingTravel.status == TravelStatus.INACTIVE) {
            return
        }

        travelDao.updateTravel(
            existingTravel.copy(
                locationName = location?.name,
                locationLatitude = location?.latitude,
                locationLongitude = location?.longitude
            )
        )
    }

    suspend fun startTravel(
        travelId: Int
    ) {
        database.withTransaction {

            val existingTravel = travelDao
                .getTravelWithCategories(travelId)
                ?.travel
                ?: return@withTransaction

            val today = LocalDate.now()

            val startDate = existingTravel.date
                ?.takeIf { scheduledDate ->
                    !scheduledDate.isAfter(today)
                }
                ?: today

            travelDao.uncheckAllItemsForTravel(travelId)

            travelDao.updateTravel(
                existingTravel.copy(
                    status = TravelStatus.ACTIVE,
                    date = null,
                    startDate = startDate
                )
            )
        }
    }

    suspend fun finishTravel(
        travelId: Int,
        endDate: LocalDate = LocalDate.now()
    ) {
        database.withTransaction {

            val existingTravel = travelDao
                .getTravelWithCategories(travelId)
                ?.travel
                ?: return@withTransaction

            val startDate = existingTravel.startDate
                ?: existingTravel.date
                ?: endDate

            require(!endDate.isBefore(startDate)) {
                "End date cannot be before start date."
            }

            database.travelHistoryDao().insertHistory(
                TravelHistoryEntity(
                    title = existingTravel.name,
                    locationName = existingTravel.locationName,
                    startDate = startDate,
                    endDate = endDate
                )
            )

            travelDao.uncheckAllItemsForTravel(travelId)

            travelDao.updateTravel(
                existingTravel.copy(
                    status = TravelStatus.INACTIVE,
                    date = null,
                    startDate = null,
                    locationName = null,
                    locationLatitude = null,
                    locationLongitude = null
                )
            )
        }
    }
}
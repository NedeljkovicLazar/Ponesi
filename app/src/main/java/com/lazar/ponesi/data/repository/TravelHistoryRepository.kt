package com.lazar.ponesi.data.repository

import com.lazar.ponesi.data.database.AppDatabase
import com.lazar.ponesi.data.database.entity.TravelHistoryEntity
import com.lazar.ponesi.data.model.TravelHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class TravelHistoryRepository(
    database: AppDatabase
) {
    private val historyDao = database.travelHistoryDao()

    fun getAllHistory(): Flow<List<TravelHistory>> {
        return historyDao
            .getAllHistory()
            .map { historyEntries ->
                historyEntries.map { entity ->
                    entity.toTravelHistory()
                }
            }
    }

    fun getHistory(historyId: Int): Flow<TravelHistory?> {
        return historyDao
            .getHistory(historyId)
            .map { entity ->
                entity?.toTravelHistory()
            }
    }

    suspend fun updateHistory(
        historyId: Int,
        title: String,
        locationName: String?,
        startDate: LocalDate,
        endDate: LocalDate,
        photoUri: String?
    ) {
        historyDao.updateHistory(
            TravelHistoryEntity(
                id = historyId,
                title = title.trim(),
                locationName = locationName
                    ?.trim()
                    ?.takeIf { it.isNotBlank() },
                startDate = startDate,
                endDate = endDate,
                photoUri = photoUri
            )
        )
    }

    suspend fun deleteHistory(history: TravelHistory) {
        historyDao.deleteHistory(
            TravelHistoryEntity(
                id = history.id,
                title = history.title,
                locationName = history.locationName,
                startDate = history.startDate,
                endDate = history.endDate,
                photoUri = history.photoUri
            )
        )
    }

    private fun TravelHistoryEntity.toTravelHistory(): TravelHistory {
        return TravelHistory(
            id = id,
            title = title,
            locationName = locationName,
            startDate = startDate,
            endDate = endDate,
            photoUri = photoUri
        )
    }
}
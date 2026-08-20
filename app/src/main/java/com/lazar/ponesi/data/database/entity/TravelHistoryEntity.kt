package com.lazar.ponesi.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "travel_history"
)
data class TravelHistoryEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    val locationName: String?,

    val startDate: LocalDate,

    val endDate: LocalDate,

    val photoUri: String? = null
)
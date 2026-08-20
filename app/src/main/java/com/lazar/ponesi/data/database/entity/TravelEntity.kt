package com.lazar.ponesi.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lazar.ponesi.data.model.TravelStatus
import java.time.LocalDate

@Entity(
    tableName = "travels"
)
data class TravelEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    val status: TravelStatus = TravelStatus.INACTIVE,

    val date: LocalDate? = null,

    val startDate: LocalDate? = null,

    val locationName: String? = null,

    val locationLatitude: Double? = null,

    val locationLongitude: Double? = null
)
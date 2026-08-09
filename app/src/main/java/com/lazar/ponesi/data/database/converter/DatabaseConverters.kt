package com.lazar.ponesi.data.database.converter

import androidx.room.TypeConverter
import com.lazar.ponesi.data.model.TravelStatus
import java.time.LocalDate

class DatabaseConverters {

    @TypeConverter
    fun fromTravelStatus(status: TravelStatus): String {
        return status.name
    }

    @TypeConverter
    fun toTravelStatus(value: String): TravelStatus {
        return TravelStatus.valueOf(value)
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let {
            LocalDate.parse(it)
        }
    }
}
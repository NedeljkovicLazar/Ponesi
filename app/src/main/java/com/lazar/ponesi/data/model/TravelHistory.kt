package com.lazar.ponesi.data.model

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class TravelHistory(
    val id: Int,
    val title: String,
    val locationName: String?,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val photoUri: String?
) {
    val durationDays: Long
        get() = ChronoUnit.DAYS.between(
            startDate,
            endDate
        ) + 1
}
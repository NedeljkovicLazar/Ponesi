package com.lazar.ponesi.data.model

import java.time.LocalDate

data class Travel(
    val id: Int,
    val name: String,
    val status: TravelStatus,
    val categories: List<Category> = emptyList(),
    val date: LocalDate? = null
)
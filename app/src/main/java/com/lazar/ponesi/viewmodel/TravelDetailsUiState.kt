package com.lazar.ponesi.viewmodel

import com.lazar.ponesi.data.model.Travel

data class TravelDetailsUiState(
    val travel: Travel? = null,
    val isLoading: Boolean = true
)
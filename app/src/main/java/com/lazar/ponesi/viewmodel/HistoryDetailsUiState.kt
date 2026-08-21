package com.lazar.ponesi.viewmodel

import com.lazar.ponesi.data.model.TravelHistory

data class HistoryDetailsUiState(
    val history: TravelHistory? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isDeleted: Boolean = false
)
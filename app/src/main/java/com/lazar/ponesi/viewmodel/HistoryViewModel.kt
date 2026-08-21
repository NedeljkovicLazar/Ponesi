package com.lazar.ponesi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazar.ponesi.data.model.TravelHistory
import com.lazar.ponesi.data.repository.TravelHistoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(
    travelHistoryRepository: TravelHistoryRepository
) : ViewModel() {

    val history: StateFlow<List<TravelHistory>> =
        travelHistoryRepository
            .getAllHistory()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}
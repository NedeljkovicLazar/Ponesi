package com.lazar.ponesi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazar.ponesi.data.repository.TravelHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class HistoryDetailsViewModel(
    private val travelHistoryRepository: TravelHistoryRepository,
    private val historyId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        HistoryDetailsUiState()
    )

    val uiState: StateFlow<HistoryDetailsUiState> =
        _uiState.asStateFlow()

    init {
        observeHistory()
    }

    private fun observeHistory() {
        viewModelScope.launch {
            travelHistoryRepository
                .getHistory(historyId)
                .collect { history ->
                    _uiState.value = _uiState.value.copy(
                        history = history,
                        isLoading = false
                    )
                }
        }
    }

    fun updateHistory(
        title: String,
        locationName: String?,
        startDate: LocalDate,
        endDate: LocalDate,
        photoUri: String?
    ) {
        if (
            title.isBlank() ||
            endDate.isBefore(startDate) ||
            endDate.isAfter(LocalDate.now()) ||
            _uiState.value.isSaving
        ) {
            return
        }

        _uiState.value = _uiState.value.copy(
            isSaving = true
        )

        viewModelScope.launch {
            travelHistoryRepository.updateHistory(
                historyId = historyId,
                title = title,
                locationName = locationName,
                startDate = startDate,
                endDate = endDate,
                photoUri = photoUri
            )

            _uiState.value = _uiState.value.copy(
                isSaving = false
            )
        }
    }

    fun deleteHistory() {
        val history = _uiState.value.history
            ?: return

        viewModelScope.launch {
            travelHistoryRepository.deleteHistory(history)

            _uiState.value = _uiState.value.copy(
                history = null,
                isDeleted = true
            )
        }
    }
}
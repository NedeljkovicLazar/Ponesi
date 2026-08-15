package com.lazar.ponesi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazar.ponesi.data.model.TravelStatus
import com.lazar.ponesi.data.repository.TravelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class TravelDetailsViewModel(
    private val travelRepository: TravelRepository,
    private val travelId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        TravelDetailsUiState()
    )

    val uiState: StateFlow<TravelDetailsUiState> =
        _uiState.asStateFlow()

    init {
        loadTravel()
    }

    private fun loadTravel() {
        viewModelScope.launch {

            val travel = travelRepository.getTravel(travelId)

            _uiState.value = TravelDetailsUiState(
                travel = travel,
                isLoading = false
            )
        }
    }

    fun onItemCheckedChange(
        categoryId: Int,
        itemId: Int,
        isChecked: Boolean
    ) {
        val currentTravel = _uiState.value.travel
            ?: return

        val updatedCategories = currentTravel.categories.map { category ->

            if (category.id == categoryId) {
                category.copy(
                    items = category.items.map { item ->

                        if (item.id == itemId) {
                            item.copy(
                                isChecked = isChecked
                            )
                        } else {
                            item
                        }
                    }
                )
            } else {
                category
            }
        }

        _uiState.value = _uiState.value.copy(
            travel = currentTravel.copy(
                categories = updatedCategories
            )
        )

        viewModelScope.launch {
            travelRepository.updatePackingItemChecked(
                itemId = itemId,
                isChecked = isChecked
            )
        }
    }

    fun setCategoryItemsChecked(
        categoryId: Int,
        isChecked: Boolean
    ) {
        val currentTravel = _uiState.value.travel
            ?: return

        val updatedCategories = currentTravel.categories.map { category ->

            if (category.id == categoryId) {
                category.copy(
                    items = category.items.map { item ->
                        item.copy(
                            isChecked = isChecked
                        )
                    }
                )
            } else {
                category
            }
        }

        _uiState.value = _uiState.value.copy(
            travel = currentTravel.copy(
                categories = updatedCategories
            )
        )

        viewModelScope.launch {
            travelRepository.updateCategoryItemsChecked(
                categoryId = categoryId,
                isChecked = isChecked
            )
        }
    }

    fun scheduleTravel(date: LocalDate) {
        viewModelScope.launch {

            travelRepository.scheduleTravel(
                travelId = travelId,
                date = date
            )

            updateTravelState(
                status = TravelStatus.SCHEDULED,
                date = date,
                uncheckItems = false
            )
        }
    }

    fun cancelScheduledTravel() {
        viewModelScope.launch {

            travelRepository.cancelScheduledTravel(travelId)

            updateTravelState(
                status = TravelStatus.INACTIVE,
                date = null,
                uncheckItems = false
            )
        }
    }

    fun startTravel() {
        viewModelScope.launch {

            travelRepository.startTravel(travelId)

            updateTravelState(
                status = TravelStatus.ACTIVE,
                date = null,
                uncheckItems = true
            )
        }
    }

    fun finishTravel() {
        viewModelScope.launch {

            travelRepository.finishTravel(travelId)

            updateTravelState(
                status = TravelStatus.INACTIVE,
                date = null,
                uncheckItems = true
            )
        }
    }

    private fun updateTravelState(
        status: TravelStatus,
        date: LocalDate?,
        uncheckItems: Boolean
    ) {
        _uiState.update { currentState ->

            val currentTravel = currentState.travel
                ?: return@update currentState

            val updatedCategories =
                if (uncheckItems) {
                    currentTravel.categories.map { category ->
                        category.copy(
                            items = category.items.map { item ->
                                item.copy(isChecked = false)
                            }
                        )
                    }
                } else {
                    currentTravel.categories
                }

            currentState.copy(
                travel = currentTravel.copy(
                    status = status,
                    date = date,
                    categories = updatedCategories
                )
            )
        }
    }
}
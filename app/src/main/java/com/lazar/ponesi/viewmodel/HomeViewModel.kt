package com.lazar.ponesi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazar.ponesi.data.model.Travel
import com.lazar.ponesi.data.repository.TravelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val travelRepository: TravelRepository
) : ViewModel() {

    val travels: Flow<List<Travel>> =
        travelRepository.getAllTravels()

    fun deleteTravel(travelId: Int) {
        viewModelScope.launch {
            travelRepository.deleteTravel(travelId)
        }
    }
}
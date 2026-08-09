package com.lazar.ponesi.viewmodel

import androidx.lifecycle.ViewModel
import com.lazar.ponesi.data.model.Travel
import com.lazar.ponesi.data.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class HomeViewModel(
    travelRepository: TravelRepository
) : ViewModel() {

    val travels: Flow<List<Travel>> =
        travelRepository.getAllTravels()
}
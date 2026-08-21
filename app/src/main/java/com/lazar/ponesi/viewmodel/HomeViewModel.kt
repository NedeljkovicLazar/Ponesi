package com.lazar.ponesi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazar.ponesi.data.location.CurrentLocationProvider
import com.lazar.ponesi.data.model.Travel
import com.lazar.ponesi.data.model.TravelFilter
import com.lazar.ponesi.data.model.TravelSort
import com.lazar.ponesi.data.model.TravelStatus
import com.lazar.ponesi.data.repository.TravelRepository
import com.lazar.ponesi.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.util.Locale

class HomeViewModel(
    private val travelRepository: TravelRepository,
    private val weatherRepository: WeatherRepository,
    private val currentLocationProvider: CurrentLocationProvider
) : ViewModel() {

    private val _weatherUiState = MutableStateFlow(
        WeatherUiState()
    )

    val weatherUiState: StateFlow<WeatherUiState> =
        _weatherUiState.asStateFlow()

    private val _selectedFilter = MutableStateFlow(
        TravelFilter.ALL
    )

    val selectedFilter: StateFlow<TravelFilter> =
        _selectedFilter.asStateFlow()

    private val _selectedSort = MutableStateFlow(
        TravelSort.NEWEST
    )

    val selectedSort: StateFlow<TravelSort> =
        _selectedSort.asStateFlow()

    val travels: StateFlow<List<Travel>> =
        combine(
            travelRepository.getAllTravels(),
            _selectedFilter,
            _selectedSort
        ) { travels, selectedFilter, selectedSort ->

            val filteredTravels = when (selectedFilter) {
                TravelFilter.ALL -> {
                    travels
                }

                TravelFilter.ACTIVE -> {
                    travels.filter { travel ->
                        travel.status == TravelStatus.ACTIVE
                    }
                }

                TravelFilter.SCHEDULED -> {
                    travels.filter { travel ->
                        travel.status == TravelStatus.SCHEDULED
                    }
                }

                TravelFilter.INACTIVE -> {
                    travels.filter { travel ->
                        travel.status == TravelStatus.INACTIVE
                    }
                }
            }

            when (selectedSort) {
                TravelSort.NEWEST -> {
                    filteredTravels.sortedByDescending { travel ->
                        travel.id
                    }
                }

                TravelSort.NAME -> {
                    filteredTravels.sortedBy { travel ->
                        travel.name.lowercase()
                    }
                }

                TravelSort.DATE -> {
                    filteredTravels.sortedWith(
                        compareBy<Travel> { travel ->
                            if (travel.date == null) {
                                1
                            } else {
                                0
                            }
                        }
                            .thenBy { travel ->
                                travel.date
                            }
                            .thenBy { travel ->
                                travel.name.lowercase()
                            }
                    )
                }

                TravelSort.STATUS -> {
                    filteredTravels.sortedWith(
                        compareBy<Travel> { travel ->
                            statusOrder(travel.status)
                        }
                            .thenBy { travel ->
                                travel.name.lowercase()
                            }
                    )
                }
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun selectFilter(filter: TravelFilter) {
        _selectedFilter.value = filter
    }

    fun selectSort(sort: TravelSort) {
        _selectedSort.value = sort
    }

    fun deleteTravel(travelId: Int) {
        viewModelScope.launch {
            travelRepository.deleteTravel(travelId)
        }
    }

    fun loadCurrentLocationWeather() {
        if (_weatherUiState.value.isLoading) {
            return
        }

        _weatherUiState.value = _weatherUiState.value.copy(
            isLoading = true,
            error = null
        )

        viewModelScope.launch {
            try {
                val location = withTimeoutOrNull(15_000) {
                    currentLocationProvider.getCurrentLocation()
                }

                if (location == null) {
                    _weatherUiState.value = WeatherUiState(
                        error = WeatherError.LOCATION_UNAVAILABLE
                    )

                    return@launch
                }

                val result = weatherRepository.getForecast(
                    cacheKey = String.format(
                        Locale.US,
                        "current_%.2f_%.2f",
                        location.latitude,
                        location.longitude
                    ),
                    latitude = location.latitude,
                    longitude = location.longitude
                )

                _weatherUiState.value = WeatherUiState(
                    forecast = result.forecast,
                    isFromCache = result.isFromCache
                )
            } catch (_: Exception) {
                _weatherUiState.value = WeatherUiState(
                    error = WeatherError.NETWORK_UNAVAILABLE
                )
            }
        }
    }

    private fun statusOrder(
        status: TravelStatus
    ): Int {
        return when (status) {
            TravelStatus.ACTIVE -> 0
            TravelStatus.SCHEDULED -> 1
            TravelStatus.INACTIVE -> 2
        }
    }
}
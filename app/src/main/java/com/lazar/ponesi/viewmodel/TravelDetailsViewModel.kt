package com.lazar.ponesi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazar.ponesi.data.model.LocationSuggestion
import com.lazar.ponesi.data.model.Travel
import com.lazar.ponesi.data.model.TravelStatus
import com.lazar.ponesi.data.repository.TravelRepository
import com.lazar.ponesi.data.repository.WeatherRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class TravelDetailsViewModel(
    private val travelRepository: TravelRepository,
    private val weatherRepository: WeatherRepository,
    private val travelId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        TravelDetailsUiState()
    )

    val uiState: StateFlow<TravelDetailsUiState> =
        _uiState.asStateFlow()

    private var locationSearchJob: Job? = null
    private var weatherJob: Job? = null

    init {
        loadTravel()
    }

    private fun loadTravel() {
        viewModelScope.launch {
            val travel = travelRepository.getTravel(travelId)

            _uiState.value = _uiState.value.copy(
                travel = travel,
                isLoading = false
            )

            travel?.let { loadedTravel ->
                loadDestinationWeather(loadedTravel)
            }
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
                            item.copy(isChecked = isChecked)
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
                        item.copy(isChecked = isChecked)
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
        val currentTravel = _uiState.value.travel
            ?: return

        viewModelScope.launch {
            travelRepository.scheduleTravel(
                travelId = travelId,
                date = date
            )

            val updatedTravel = currentTravel.copy(
                status = TravelStatus.SCHEDULED,
                date = date,
                startDate = null
            )

            _uiState.value = _uiState.value.copy(
                travel = updatedTravel
            )

            loadDestinationWeather(updatedTravel)
        }
    }

    fun cancelScheduledTravel() {
        val currentTravel = _uiState.value.travel
            ?: return

        viewModelScope.launch {
            travelRepository.cancelScheduledTravel(travelId)

            _uiState.value = _uiState.value.copy(
                travel = currentTravel.copy(
                    status = TravelStatus.INACTIVE,
                    date = null,
                    startDate = null,
                    location = null
                ),
                destinationWeather = WeatherUiState()
            )
        }
    }

    fun startTravel() {
        val currentTravel = _uiState.value.travel
            ?: return

        val today = LocalDate.now()

        val startDate = currentTravel.date
            ?.takeIf { scheduledDate ->
                !scheduledDate.isAfter(today)
            }
            ?: today

        viewModelScope.launch {
            travelRepository.startTravel(travelId)

            val updatedCategories = currentTravel.categories.map { category ->
                category.copy(
                    items = category.items.map { item ->
                        item.copy(isChecked = false)
                    }
                )
            }

            val updatedTravel = currentTravel.copy(
                status = TravelStatus.ACTIVE,
                date = null,
                startDate = startDate,
                categories = updatedCategories
            )

            _uiState.value = _uiState.value.copy(
                travel = updatedTravel
            )

            loadDestinationWeather(updatedTravel)
        }
    }

    fun finishTravel(endDate: LocalDate) {
        val currentTravel = _uiState.value.travel
            ?: return

        val startDate = currentTravel.startDate
            ?: endDate

        if (
            endDate.isBefore(startDate) ||
            endDate.isAfter(LocalDate.now())
        ) {
            return
        }

        viewModelScope.launch {
            travelRepository.finishTravel(
                travelId = travelId,
                endDate = endDate
            )

            val updatedCategories = currentTravel.categories.map { category ->
                category.copy(
                    items = category.items.map { item ->
                        item.copy(isChecked = false)
                    }
                )
            }

            _uiState.value = _uiState.value.copy(
                travel = currentTravel.copy(
                    status = TravelStatus.INACTIVE,
                    date = null,
                    startDate = null,
                    location = null,
                    categories = updatedCategories
                ),
                destinationWeather = WeatherUiState()
            )
        }
    }

    fun onLocationQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(
            locationSearch = _uiState.value.locationSearch.copy(
                query = query,
                hasSearchError = false
            )
        )

        locationSearchJob?.cancel()

        if (query.trim().length < 2) {
            _uiState.value = _uiState.value.copy(
                locationSearch = _uiState.value.locationSearch.copy(
                    results = emptyList(),
                    isLoading = false
                )
            )

            return
        }

        locationSearchJob = viewModelScope.launch {
            delay(400)

            _uiState.value = _uiState.value.copy(
                locationSearch = _uiState.value.locationSearch.copy(
                    isLoading = true
                )
            )

            try {
                val results = weatherRepository.searchLocations(query)

                _uiState.value = _uiState.value.copy(
                    locationSearch = _uiState.value.locationSearch.copy(
                        results = results,
                        isLoading = false,
                        hasSearchError = false
                    )
                )
            } catch (exception: Exception) {
                if (exception is CancellationException) {
                    throw exception
                }

                _uiState.value = _uiState.value.copy(
                    locationSearch = _uiState.value.locationSearch.copy(
                        results = emptyList(),
                        isLoading = false,
                        hasSearchError = true
                    )
                )
            }
        }
    }

    fun clearLocationSearch() {
        locationSearchJob?.cancel()

        _uiState.value = _uiState.value.copy(
            locationSearch = LocationSearchUiState()
        )
    }

    fun selectLocation(location: LocationSuggestion) {
        val currentTravel = _uiState.value.travel
            ?: return

        val travelLocation = location.toTravelLocation()

        viewModelScope.launch {
            travelRepository.updateTravelLocation(
                travelId = travelId,
                location = travelLocation
            )

            val updatedTravel = currentTravel.copy(
                location = travelLocation
            )

            _uiState.value = _uiState.value.copy(
                travel = updatedTravel,
                locationSearch = LocationSearchUiState()
            )

            loadDestinationWeather(updatedTravel)
        }
    }

    fun removeLocation() {
        val currentTravel = _uiState.value.travel
            ?: return

        viewModelScope.launch {
            travelRepository.updateTravelLocation(
                travelId = travelId,
                location = null
            )

            _uiState.value = _uiState.value.copy(
                travel = currentTravel.copy(
                    location = null
                ),
                destinationWeather = WeatherUiState(),
                locationSearch = LocationSearchUiState()
            )
        }
    }

    fun refreshDestinationWeather() {
        val travel = _uiState.value.travel
            ?: return

        loadDestinationWeather(travel)
    }

    private fun loadDestinationWeather(travel: Travel) {
        weatherJob?.cancel()

        val location = travel.location

        if (
            location == null ||
            travel.status == TravelStatus.INACTIVE
        ) {
            _uiState.value = _uiState.value.copy(
                destinationWeather = WeatherUiState()
            )

            return
        }

        val latestForecastDate = LocalDate.now().plusDays(15)

        if (
            travel.status == TravelStatus.SCHEDULED &&
            travel.date?.isAfter(latestForecastDate) == true
        ) {
            _uiState.value = _uiState.value.copy(
                destinationWeather = WeatherUiState(
                    error = WeatherError.FORECAST_NOT_YET_AVAILABLE
                )
            )

            return
        }

        _uiState.value = _uiState.value.copy(
            destinationWeather = WeatherUiState(
                isLoading = true
            )
        )

        weatherJob = viewModelScope.launch {
            try {
                val result = weatherRepository.getForecast(
                    cacheKey = "travel_${travel.id}_" +
                            "${location.latitude}_${location.longitude}",
                    latitude = location.latitude,
                    longitude = location.longitude
                )

                val scheduledDate = travel.date

                if (
                    travel.status == TravelStatus.SCHEDULED &&
                    scheduledDate != null &&
                    scheduledDate.isAfter(LocalDate.now()) &&
                    result.forecast.days.none { day ->
                        day.date == scheduledDate
                    }
                ) {
                    _uiState.value = _uiState.value.copy(
                        destinationWeather = WeatherUiState(
                            error = WeatherError
                                .FORECAST_NOT_YET_AVAILABLE
                        )
                    )

                    return@launch
                }

                _uiState.value = _uiState.value.copy(
                    destinationWeather = WeatherUiState(
                        forecast = result.forecast,
                        isFromCache = result.isFromCache
                    )
                )
            } catch (exception: Exception) {
                if (exception is CancellationException) {
                    throw exception
                }

                _uiState.value = _uiState.value.copy(
                    destinationWeather = WeatherUiState(
                        error = WeatherError.NETWORK_UNAVAILABLE
                    )
                )
            }
        }
    }
}
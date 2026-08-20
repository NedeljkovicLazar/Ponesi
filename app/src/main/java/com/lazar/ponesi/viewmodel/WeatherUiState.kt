package com.lazar.ponesi.viewmodel

import com.lazar.ponesi.data.model.LocationSuggestion
import com.lazar.ponesi.data.model.WeatherForecast

data class WeatherUiState(
    val forecast: WeatherForecast? = null,
    val isLoading: Boolean = false,
    val isFromCache: Boolean = false,
    val error: WeatherError? = null
)

enum class WeatherError {
    NETWORK_UNAVAILABLE,
    LOCATION_UNAVAILABLE,
    FORECAST_NOT_YET_AVAILABLE
}

data class LocationSearchUiState(
    val query: String = "",
    val results: List<LocationSuggestion> = emptyList(),
    val isLoading: Boolean = false,
    val hasSearchError: Boolean = false
)
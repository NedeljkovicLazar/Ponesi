package com.lazar.ponesi.data.model

import java.time.LocalDate

data class WeatherForecast(
    val days: List<DailyWeather>,
    val fetchedAtMillis: Long
)

data class DailyWeather(
    val date: LocalDate,
    val weatherCode: Int,
    val minimumTemperature: Double,
    val maximumTemperature: Double,
    val precipitationProbability: Int
)

data class WeatherLoadResult(
    val forecast: WeatherForecast,
    val isFromCache: Boolean
)
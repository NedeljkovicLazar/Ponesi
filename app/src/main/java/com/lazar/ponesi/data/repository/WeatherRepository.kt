package com.lazar.ponesi.data.repository

import android.content.Context
import com.lazar.ponesi.data.model.LocationSuggestion
import com.lazar.ponesi.data.model.WeatherLoadResult
import com.lazar.ponesi.data.remote.OpenMeteoService
import kotlinx.coroutines.CancellationException

class WeatherRepository(
    context: Context,
    private val openMeteoService: OpenMeteoService = OpenMeteoService()
) {
    private val preferences = context.getSharedPreferences(
        "weather_cache",
        Context.MODE_PRIVATE
    )

    suspend fun searchLocations(
        query: String
    ): List<LocationSuggestion> {
        return openMeteoService.searchLocations(query)
    }

    suspend fun getForecast(
        cacheKey: String,
        latitude: Double,
        longitude: Double
    ): WeatherLoadResult {
        return try {
            val fetchedAt = System.currentTimeMillis()

            val response = openMeteoService.getForecastResponse(
                latitude = latitude,
                longitude = longitude
            )

            val forecast = openMeteoService.parseForecast(
                response = response,
                fetchedAtMillis = fetchedAt
            )

            preferences.edit()
                .putString(responseKey(cacheKey), response)
                .putLong(timeKey(cacheKey), fetchedAt)
                .apply()

            WeatherLoadResult(
                forecast = forecast,
                isFromCache = false
            )
        } catch (exception: Exception) {
            if (exception is CancellationException) {
                throw exception
            }

            loadCachedForecast(cacheKey) ?: throw exception
        }
    }

    private fun loadCachedForecast(
        cacheKey: String
    ): WeatherLoadResult? {
        val cachedResponse = preferences.getString(
            responseKey(cacheKey),
            null
        ) ?: return null

        return try {
            WeatherLoadResult(
                forecast = openMeteoService.parseForecast(
                    response = cachedResponse,
                    fetchedAtMillis = preferences.getLong(
                        timeKey(cacheKey),
                        0L
                    )
                ),
                isFromCache = true
            )
        } catch (_: Exception) {
            preferences.edit()
                .remove(responseKey(cacheKey))
                .remove(timeKey(cacheKey))
                .apply()

            null
        }
    }

    private fun responseKey(cacheKey: String): String {
        return "${cacheKey}_response"
    }

    private fun timeKey(cacheKey: String): String {
        return "${cacheKey}_time"
    }
}
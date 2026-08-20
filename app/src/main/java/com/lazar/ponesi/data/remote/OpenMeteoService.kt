package com.lazar.ponesi.data.remote

import com.lazar.ponesi.data.model.DailyWeather
import com.lazar.ponesi.data.model.LocationSuggestion
import com.lazar.ponesi.data.model.WeatherForecast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate

class OpenMeteoService {

    suspend fun searchLocations(
        query: String
    ): List<LocationSuggestion> = withContext(Dispatchers.IO) {
        val encodedQuery = URLEncoder.encode(
            query.trim(),
            StandardCharsets.UTF_8.toString()
        )

        val response = request(
            "https://geocoding-api.open-meteo.com/v1/search" +
                    "?name=$encodedQuery" +
                    "&count=8" +
                    "&language=sr" +
                    "&format=json"
        )

        val results = JSONObject(response)
            .optJSONArray("results")
            ?: return@withContext emptyList()

        buildList {
            for (index in 0 until results.length()) {
                val result = results.getJSONObject(index)

                add(
                    LocationSuggestion(
                        name = result.getString("name"),
                        administrativeArea = result
                            .optString("admin1")
                            .takeIf { it.isNotBlank() },
                        country = result
                            .optString("country")
                            .takeIf { it.isNotBlank() },
                        latitude = result.getDouble("latitude"),
                        longitude = result.getDouble("longitude")
                    )
                )
            }
        }
    }

    suspend fun getForecastResponse(
        latitude: Double,
        longitude: Double
    ): String = withContext(Dispatchers.IO) {
        request(
            "https://api.open-meteo.com/v1/forecast" +
                    "?latitude=$latitude" +
                    "&longitude=$longitude" +
                    "&daily=weather_code,temperature_2m_max," +
                    "temperature_2m_min,precipitation_probability_max" +
                    "&timezone=auto" +
                    "&forecast_days=16"
        )
    }

    fun parseForecast(
        response: String,
        fetchedAtMillis: Long
    ): WeatherForecast {
        val daily = JSONObject(response).getJSONObject("daily")
        val dates = daily.getJSONArray("time")
        val weatherCodes = daily.getJSONArray("weather_code")
        val maximumTemperatures = daily.getJSONArray(
            "temperature_2m_max"
        )
        val minimumTemperatures = daily.getJSONArray(
            "temperature_2m_min"
        )
        val precipitationProbabilities = daily.getJSONArray(
            "precipitation_probability_max"
        )

        val days = buildList {
            for (index in 0 until dates.length()) {
                add(
                    DailyWeather(
                        date = LocalDate.parse(
                            dates.getString(index)
                        ),
                        weatherCode = weatherCodes.getInt(index),
                        minimumTemperature = minimumTemperatures
                            .getDouble(index),
                        maximumTemperature = maximumTemperatures
                            .getDouble(index),
                        precipitationProbability =
                            if (precipitationProbabilities.isNull(index)) {
                                0
                            } else {
                                precipitationProbabilities.getInt(index)
                            }
                    )
                )
            }
        }

        return WeatherForecast(
            days = days,
            fetchedAtMillis = fetchedAtMillis
        )
    }

    private fun request(url: String): String {
        val connection = URL(url).openConnection() as HttpURLConnection

        return try {
            connection.requestMethod = "GET"
            connection.connectTimeout = 10_000
            connection.readTimeout = 10_000
            connection.setRequestProperty(
                "Accept",
                "application/json"
            )

            val responseCode = connection.responseCode

            if (responseCode !in 200..299) {
                throw IOException(
                    "Open-Meteo request failed with code $responseCode."
                )
            }

            connection.inputStream
                .bufferedReader()
                .use { reader ->
                    reader.readText()
                }
        } finally {
            connection.disconnect()
        }
    }
}
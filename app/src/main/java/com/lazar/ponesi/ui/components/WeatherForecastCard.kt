package com.lazar.ponesi.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lazar.ponesi.R
import com.lazar.ponesi.data.model.DailyWeather
import com.lazar.ponesi.ui.theme.Dimens
import com.lazar.ponesi.viewmodel.WeatherError
import com.lazar.ponesi.viewmodel.WeatherUiState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun WeatherForecastCard(
    title: String,
    weatherState: WeatherUiState,
    highlightedDate: LocalDate? = null,
    onRetryClick: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CardCornerRadius),
        border = BorderStroke(
            Dimens.ThinBorderWidth,
            MaterialTheme.colorScheme.outlineVariant
        ),
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier.padding(Dimens.SpacingLarge),
            verticalArrangement = Arrangement.spacedBy(
                Dimens.SpacingMedium
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                if (
                    !weatherState.isLoading &&
                    weatherState.error !=
                    WeatherError.FORECAST_NOT_YET_AVAILABLE
                ) {
                    IconButton(
                        onClick = onRetryClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(
                                R.string.action_refresh_weather
                            )
                        )
                    }
                }
            }

            when {
                weatherState.isLoading -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                weatherState.forecast != null -> {
                    val visibleDays = selectVisibleDays(
                        days = weatherState.forecast.days,
                        highlightedDate = highlightedDate
                    )

                    if (weatherState.isFromCache) {
                        val updatedAt = Instant.ofEpochMilli(
                            weatherState.forecast.fetchedAtMillis
                        )
                            .atZone(ZoneId.systemDefault())
                            .format(
                                DateTimeFormatter.ofPattern(
                                    "dd.MM. HH:mm"
                                )
                            )

                        Text(
                            text = stringResource(
                                R.string.message_cached_weather,
                                updatedAt
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(
                            Dimens.SpacingSmall
                        )
                    ) {
                        items(
                            items = visibleDays,
                            key = { day ->
                                day.date.toString()
                            }
                        ) { day ->
                            WeatherDayCard(
                                day = day,
                                isHighlighted = day.date == highlightedDate
                            )
                        }
                    }
                }

                else -> {
                    Text(
                        text = when (weatherState.error) {
                            WeatherError.FORECAST_NOT_YET_AVAILABLE ->
                                stringResource(
                                    R.string.message_forecast_not_yet_available
                                )

                            WeatherError.LOCATION_UNAVAILABLE ->
                                stringResource(
                                    R.string.message_current_location_unavailable
                                )

                            else ->
                                stringResource(
                                    R.string.message_weather_unavailable
                                )
                        },
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            TextButton(
                onClick = {
                    uriHandler.openUri("https://open-meteo.com/")
                }
            ) {
                Text(
                    text = stringResource(
                        R.string.weather_attribution
                    ),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
private fun WeatherDayCard(
    day: DailyWeather,
    isHighlighted: Boolean
) {
    val locale = Locale.forLanguageTag("sr-Latn-RS")
    val dateFormatter = DateTimeFormatter.ofPattern(
        "EEE d.M.",
        locale
    )

    val icon = weatherIcon(day.weatherCode)

    Surface(
        shape = RoundedCornerShape(Dimens.ButtonCornerRadius),
        color = if (isHighlighted) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        border = if (isHighlighted) {
            BorderStroke(
                Dimens.StatusBorderWidth,
                MaterialTheme.colorScheme.primary
            )
        } else {
            null
        }
    ) {
        Column(
            modifier = Modifier.padding(Dimens.SpacingMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                Dimens.SpacingSmall
            )
        ) {
            Text(
                text = day.date
                    .format(dateFormatter)
                    .replaceFirstChar { character ->
                        character.uppercase(locale)
                    },
                style = MaterialTheme.typography.labelMedium
            )

            Icon(
                imageVector = icon,
                contentDescription = weatherDescription(day.weatherCode),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = stringResource(
                    R.string.weather_temperature_range,
                    day.minimumTemperature.roundToInt(),
                    day.maximumTemperature.roundToInt()
                ),
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.WaterDrop,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 2.dp)
                )

                Text(
                    text = stringResource(
                        R.string.weather_precipitation,
                        day.precipitationProbability
                    ),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

private fun selectVisibleDays(
    days: List<DailyWeather>,
    highlightedDate: LocalDate?
): List<DailyWeather> {
    if (days.size <= 7) {
        return days
    }

    val highlightedIndex = highlightedDate?.let { date ->
        days.indexOfFirst { day ->
            day.date == date
        }
    } ?: -1

    if (highlightedIndex < 0) {
        return days.take(7)
    }

    val startIndex = (highlightedIndex - 3)
        .coerceIn(0, days.size - 7)

    return days.subList(
        startIndex,
        startIndex + 7
    )
}

private fun weatherIcon(code: Int): ImageVector {
    return when (code) {
        0, 1 -> Icons.Default.WbSunny
        2, 3, 45, 48 -> Icons.Default.Cloud
        71, 73, 75, 77, 85, 86 -> Icons.Default.AcUnit
        95, 96, 99 -> Icons.Default.Thunderstorm
        else -> Icons.Default.WaterDrop
    }
}

@Composable
private fun weatherDescription(code: Int): String {
    return when (code) {
        0 -> stringResource(R.string.weather_clear)
        1, 2 -> stringResource(R.string.weather_partly_cloudy)
        3 -> stringResource(R.string.weather_cloudy)
        45, 48 -> stringResource(R.string.weather_fog)
        51, 53, 55, 56, 57 ->
            stringResource(R.string.weather_drizzle)

        61, 63, 65, 66, 67, 80, 81, 82 ->
            stringResource(R.string.weather_rain)

        71, 73, 75, 77, 85, 86 ->
            stringResource(R.string.weather_snow)

        95, 96, 99 ->
            stringResource(R.string.weather_thunderstorm)

        else -> stringResource(R.string.weather_changeable)
    }
}
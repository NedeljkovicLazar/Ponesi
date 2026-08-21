package com.lazar.ponesi.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lazar.ponesi.R
import com.lazar.ponesi.data.model.TravelStatus
import com.lazar.ponesi.ui.components.AppTopBar
import com.lazar.ponesi.ui.components.ChecklistCategoryCard
import com.lazar.ponesi.ui.components.LocationSearchDialog
import com.lazar.ponesi.ui.components.TravelLifecycleCard
import com.lazar.ponesi.ui.components.TravelLocationCard
import com.lazar.ponesi.ui.components.WeatherForecastCard
import com.lazar.ponesi.ui.theme.Dimens
import com.lazar.ponesi.viewmodel.TravelDetailsViewModel
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun TravelDetailsScreen(
    onBackClick: () -> Unit,
    travelDetailsViewModel: TravelDetailsViewModel
) {
    val uiState by travelDetailsViewModel
        .uiState
        .collectAsStateWithLifecycle()

    val context = LocalContext.current

    var showStartDialog by remember {
        mutableStateOf(false)
    }

    var showFinishDialog by remember {
        mutableStateOf(false)
    }

    var showLocationDialog by remember {
        mutableStateOf(false)
    }

    var automaticStartDialogShown by remember {
        mutableStateOf(false)
    }

    val currentTravel = uiState.travel

    LaunchedEffect(
        currentTravel?.status,
        currentTravel?.date
    ) {
        val scheduledDate = currentTravel?.date

        val scheduledDateHasArrived =
            currentTravel?.status == TravelStatus.SCHEDULED &&
                    scheduledDate != null &&
                    !scheduledDate.isAfter(LocalDate.now())

        if (
            scheduledDateHasArrived &&
            !automaticStartDialogShown
        ) {
            showStartDialog = true
            automaticStartDialogShown = true
        }

        if (!scheduledDateHasArrived) {
            automaticStartDialogShown = false
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = currentTravel?.name
                    ?: stringResource(
                        R.string.title_travel_details
                    ),
                showBackButton = true,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            currentTravel == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(
                            R.string.message_travel_not_found
                        )
                    )
                }
            }

            else -> {
                val travel = currentTravel

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(
                        Dimens.SpacingLarge
                    ),
                    verticalArrangement = Arrangement.spacedBy(
                        Dimens.SpacingMedium
                    )
                ) {
                    item {
                        TravelLifecycleCard(
                            travel = travel,
                            onScheduleClick = {
                                val today = LocalDate.now()

                                val initialDate = travel.date
                                    ?.takeIf { date ->
                                        !date.isBefore(today)
                                    }
                                    ?: today

                                val datePickerDialog = DatePickerDialog(
                                    context,
                                    { _, year, month, dayOfMonth ->
                                        val selectedDate = LocalDate.of(
                                            year,
                                            month + 1,
                                            dayOfMonth
                                        )

                                        travelDetailsViewModel
                                            .scheduleTravel(selectedDate)

                                        if (
                                            travel.location == null &&
                                            selectedDate.isAfter(today)
                                        ) {
                                            showLocationDialog = true
                                        }
                                    },
                                    initialDate.year,
                                    initialDate.monthValue - 1,
                                    initialDate.dayOfMonth
                                )

                                datePickerDialog.datePicker.minDate =
                                    today
                                        .atStartOfDay(
                                            ZoneId.systemDefault()
                                        )
                                        .toInstant()
                                        .toEpochMilli()

                                datePickerDialog.show()
                            },
                            onCancelScheduleClick = {
                                travelDetailsViewModel
                                    .cancelScheduledTravel()
                            },
                            onStartClick = {
                                showStartDialog = true
                            },
                            onFinishClick = {
                                showFinishDialog = true
                            }
                        )
                    }

                    if (travel.status != TravelStatus.INACTIVE) {
                        item {
                            TravelLocationCard(
                                location = travel.location,
                                onAddOrChangeClick = {
                                    showLocationDialog = true
                                },
                                onRemoveClick = {
                                    travelDetailsViewModel
                                        .removeLocation()
                                }
                            )
                        }

                        if (travel.location != null) {
                            item {
                                WeatherForecastCard(
                                    title = stringResource(
                                        R.string.title_destination_weather
                                    ),
                                    weatherState =
                                        uiState.destinationWeather,
                                    highlightedDate =
                                        if (
                                            travel.status ==
                                            TravelStatus.SCHEDULED
                                        ) {
                                            travel.date
                                        } else {
                                            null
                                        },
                                    onRetryClick = {
                                        travelDetailsViewModel
                                            .refreshDestinationWeather()
                                    }
                                )
                            }
                        }
                    }

                    items(
                        items = travel.categories,
                        key = { category ->
                            category.id
                        }
                    ) { category ->
                        ChecklistCategoryCard(
                            category = category,
                            onItemCheckedChange = {
                                    itemId,
                                    isChecked ->

                                travelDetailsViewModel
                                    .onItemCheckedChange(
                                        categoryId = category.id,
                                        itemId = itemId,
                                        isChecked = isChecked
                                    )
                            },
                            onSetAllChecked = { isChecked ->
                                travelDetailsViewModel
                                    .setCategoryItemsChecked(
                                        categoryId = category.id,
                                        isChecked = isChecked
                                    )
                            }
                        )
                    }
                }
            }
        }
    }

    if (showStartDialog) {
        AlertDialog(
            onDismissRequest = {
                showStartDialog = false
            },
            title = {
                Text(
                    text = stringResource(
                        R.string.title_start_travel,
                        currentTravel?.name.orEmpty()
                    )
                )
            },
            text = {
                Text(
                    text = stringResource(
                        R.string.message_start_travel
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        travelDetailsViewModel.startTravel()
                        showStartDialog = false
                    }
                ) {
                    Text(
                        text = stringResource(
                            R.string.action_start_travel
                        )
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showStartDialog = false
                    }
                ) {
                    Text(
                        text = stringResource(
                            R.string.action_cancel
                        )
                    )
                }
            }
        )
    }

    if (showFinishDialog && currentTravel != null) {
        AlertDialog(
            onDismissRequest = {
                showFinishDialog = false
            },
            title = {
                Text(
                    text = stringResource(
                        R.string.title_finish_travel
                    )
                )
            },
            text = {
                Text(
                    text = stringResource(
                        R.string.message_finish_travel
                    )
                )
            },
            confirmButton = {
                Row {
                    TextButton(
                        onClick = {
                            showFinishDialog = false

                            val today = LocalDate.now()
                            val startDate = currentTravel.startDate
                                ?: today

                            val datePickerDialog = DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    travelDetailsViewModel.finishTravel(
                                        LocalDate.of(
                                            year,
                                            month + 1,
                                            dayOfMonth
                                        )
                                    )
                                },
                                today.year,
                                today.monthValue - 1,
                                today.dayOfMonth
                            )

                            datePickerDialog.datePicker.minDate =
                                startDate
                                    .atStartOfDay(
                                        ZoneId.systemDefault()
                                    )
                                    .toInstant()
                                    .toEpochMilli()

                            datePickerDialog.datePicker.maxDate =
                                today
                                    .atStartOfDay(
                                        ZoneId.systemDefault()
                                    )
                                    .toInstant()
                                    .toEpochMilli()

                            datePickerDialog.show()
                        }
                    ) {
                        Text(
                            text = stringResource(
                                R.string.action_choose_date
                            )
                        )
                    }

                    TextButton(
                        onClick = {
                            travelDetailsViewModel.finishTravel(
                                LocalDate.now()
                            )
                            showFinishDialog = false
                        }
                    ) {
                        Text(
                            text = stringResource(
                                R.string.action_finish_today
                            )
                        )
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showFinishDialog = false
                    }
                ) {
                    Text(
                        text = stringResource(
                            R.string.action_cancel
                        )
                    )
                }
            }
        )
    }

    if (showLocationDialog) {
        LocationSearchDialog(
            searchState = uiState.locationSearch,
            onQueryChange = { query ->
                travelDetailsViewModel
                    .onLocationQueryChange(query)
            },
            onLocationSelected = { location ->
                travelDetailsViewModel.selectLocation(location)
                showLocationDialog = false
            },
            onDismiss = {
                travelDetailsViewModel.clearLocationSearch()
                showLocationDialog = false
            }
        )
    }
}
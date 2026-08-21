package com.lazar.ponesi.ui.screens

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.lazar.ponesi.R
import com.lazar.ponesi.ui.components.AppTopBar
import com.lazar.ponesi.ui.theme.Dimens
import com.lazar.ponesi.viewmodel.HistoryDetailsViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun HistoryDetailsScreen(
    onBackClick: () -> Unit,
    historyDetailsViewModel: HistoryDetailsViewModel
) {
    val uiState by historyDetailsViewModel
        .uiState
        .collectAsStateWithLifecycle()

    val context = LocalContext.current
    val dateFormatter = remember {
        DateTimeFormatter.ofPattern("dd.MM.yyyy.")
    }

    var isEditing by remember {
        mutableStateOf(false)
    }

    var editedTitle by remember {
        mutableStateOf("")
    }

    var editedLocation by remember {
        mutableStateOf("")
    }

    var editedStartDate by remember {
        mutableStateOf(LocalDate.now())
    }

    var editedEndDate by remember {
        mutableStateOf(LocalDate.now())
    }

    var editedPhotoUri by remember {
        mutableStateOf<String?>(null)
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    var showFullPhoto by remember {
        mutableStateOf(false)
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) {
                // Neki provideri ne podržavaju trajnu dozvolu.
            }

            editedPhotoUri = uri.toString()
        }
    }

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) {
            onBackClick()
        }
    }

    val history = uiState.history

    Scaffold(
        topBar = {
            AppTopBar(
                title = history?.title
                    ?: stringResource(
                        R.string.title_history_details
                    ),
                showBackButton = true,
                showEdit = history != null && !isEditing,
                onBackClick = onBackClick,
                onEditClick = {
                    history?.let { currentHistory ->
                        editedTitle = currentHistory.title
                        editedLocation = currentHistory
                            .locationName
                            .orEmpty()
                        editedStartDate = currentHistory.startDate
                        editedEndDate = currentHistory.endDate
                        editedPhotoUri = currentHistory.photoUri
                        isEditing = true
                    }
                }
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

            history == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(
                            R.string.message_history_not_found
                        )
                    )
                }
            }

            isEditing -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(
                        Dimens.SpacingLarge
                    ),
                    verticalArrangement = Arrangement.spacedBy(
                        Dimens.SpacingLarge
                    )
                ) {
                    item {
                        HistoryPhoto(
                            photoUri = editedPhotoUri,
                            onClick = {
                                if (editedPhotoUri != null) {
                                    showFullPhoto = true
                                }
                            }
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                Dimens.SpacingMedium
                            )
                        ) {
                            OutlinedButton(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    photoPicker.launch(
                                        arrayOf(
                                            "image/jpeg",
                                            "image/png"
                                        )
                                    )
                                }
                            ) {
                                Text(
                                    text = stringResource(
                                        if (editedPhotoUri == null) {
                                            R.string.action_add_photo
                                        } else {
                                            R.string.action_change_photo
                                        }
                                    )
                                )
                            }

                            if (editedPhotoUri != null) {
                                TextButton(
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        editedPhotoUri = null
                                    }
                                ) {
                                    Text(
                                        text = stringResource(
                                            R.string.action_remove_photo
                                        )
                                    )
                                }
                            }
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = editedTitle,
                            onValueChange = { value ->
                                editedTitle = value
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text(
                                    text = stringResource(
                                        R.string.label_history_title
                                    )
                                )
                            },
                            singleLine = true
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = editedLocation,
                            onValueChange = { value ->
                                editedLocation = value
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text(
                                    text = stringResource(
                                        R.string.label_history_location
                                    )
                                )
                            },
                            singleLine = true
                        )
                    }

                    item {
                        HistoryDateButton(
                            label = stringResource(
                                R.string.label_start_date
                            ),
                            date = editedStartDate,
                            formatter = dateFormatter,
                            onClick = {
                                showDatePicker(
                                    context = context,
                                    initialDate = editedStartDate,
                                    minimumDate = null,
                                    maximumDate = editedEndDate,
                                    onDateSelected = { date ->
                                        editedStartDate = date
                                    }
                                )
                            }
                        )
                    }

                    item {
                        HistoryDateButton(
                            label = stringResource(
                                R.string.label_end_date
                            ),
                            date = editedEndDate,
                            formatter = dateFormatter,
                            onClick = {
                                showDatePicker(
                                    context = context,
                                    initialDate = editedEndDate,
                                    minimumDate = editedStartDate,
                                    maximumDate = LocalDate.now(),
                                    onDateSelected = { date ->
                                        editedEndDate = date
                                    }
                                )
                            }
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                Dimens.SpacingMedium
                            )
                        ) {
                            OutlinedButton(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    isEditing = false
                                }
                            ) {
                                Text(
                                    text = stringResource(
                                        R.string.action_cancel
                                    )
                                )
                            }

                            Button(
                                modifier = Modifier.weight(1f),
                                enabled = editedTitle.isNotBlank() &&
                                        !editedEndDate.isBefore(
                                            editedStartDate
                                        ) &&
                                        !uiState.isSaving,
                                onClick = {
                                    historyDetailsViewModel.updateHistory(
                                        title = editedTitle,
                                        locationName = editedLocation,
                                        startDate = editedStartDate,
                                        endDate = editedEndDate,
                                        photoUri = editedPhotoUri
                                    )

                                    isEditing = false
                                }
                            ) {
                                Text(
                                    text = stringResource(
                                        R.string.action_save
                                    )
                                )
                            }
                        }
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(
                        Dimens.SpacingLarge
                    ),
                    verticalArrangement = Arrangement.spacedBy(
                        Dimens.SpacingLarge
                    )
                ) {
                    item {
                        HistoryPhoto(
                            photoUri = history.photoUri,
                            onClick = {
                                if (history.photoUri != null) {
                                    showFullPhoto = true
                                }
                            }
                        )
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(
                                Dimens.CardCornerRadius
                            ),
                            colors = CardDefaults.cardColors()
                        ) {
                            Column(
                                modifier = Modifier.padding(
                                    Dimens.SpacingLarge
                                ),
                                verticalArrangement = Arrangement.spacedBy(
                                    Dimens.SpacingMedium
                                )
                            ) {
                                HistoryDetailRow(
                                    label = stringResource(
                                        R.string.label_history_location
                                    ),
                                    value = history.locationName
                                        ?: stringResource(
                                            R.string.value_no_location
                                        )
                                )

                                HistoryDetailRow(
                                    label = stringResource(
                                        R.string.label_start_date
                                    ),
                                    value = history.startDate.format(
                                        dateFormatter
                                    )
                                )

                                HistoryDetailRow(
                                    label = stringResource(
                                        R.string.label_end_date
                                    ),
                                    value = history.endDate.format(
                                        dateFormatter
                                    )
                                )

                                HistoryDetailRow(
                                    label = stringResource(
                                        R.string.label_duration
                                    ),
                                    value = pluralStringResource(
                                        id = R.plurals.history_duration_days,
                                        count = history.durationDays.toInt(),
                                        history.durationDays
                                    )
                                )
                            }
                        }
                    }

                    item {
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor =
                                    MaterialTheme.colorScheme.error
                            ),
                            border = BorderStroke(
                                Dimens.ThinBorderWidth,
                                MaterialTheme.colorScheme.error
                            ),
                            onClick = {
                                showDeleteDialog = true
                            }
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.action_delete_history
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog && history != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text(
                    text = stringResource(
                        R.string.title_delete_history
                    )
                )
            },
            text = {
                Text(
                    text = stringResource(
                        R.string.message_delete_history,
                        history.title
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        historyDetailsViewModel.deleteHistory()
                        showDeleteDialog = false
                    }
                ) {
                    Text(
                        text = stringResource(
                            R.string.action_delete
                        )
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
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

    val fullPhotoUri = if (isEditing) {
        editedPhotoUri
    } else {
        history?.photoUri
    }

    if (showFullPhoto && fullPhotoUri != null) {
        Dialog(
            onDismissRequest = {
                showFullPhoto = false
            }
        ) {
            Surface(
                shape = RoundedCornerShape(
                    Dimens.CardCornerRadius
                )
            ) {
                Column(
                    modifier = Modifier.padding(
                        Dimens.SpacingMedium
                    ),
                    verticalArrangement = Arrangement.spacedBy(
                        Dimens.SpacingSmall
                    )
                ) {
                    AsyncImage(
                        model = fullPhotoUri,
                        contentDescription = stringResource(
                            R.string.history_photo_description
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 620.dp),
                        contentScale = ContentScale.Fit
                    )

                    TextButton(
                        modifier = Modifier.align(
                            Alignment.End
                        ),
                        onClick = {
                            showFullPhoto = false
                        }
                    ) {
                        Text(
                            text = stringResource(
                                R.string.action_close
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryPhoto(
    photoUri: String?,
    onClick: () -> Unit
) {
    val modifier = Modifier
        .fillMaxWidth()
        .height(220.dp)
        .clip(
            RoundedCornerShape(Dimens.CardCornerRadius)
        )

    if (photoUri == null) {
        Box(
            modifier = modifier.background(
                MaterialTheme.colorScheme.surfaceVariant
            ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    Dimens.SpacingSmall
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null
                )

                Text(
                    text = stringResource(
                        R.string.message_no_history_photo
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        AsyncImage(
            model = photoUri.toUri(),
            contentDescription = stringResource(
                R.string.history_photo_description
            ),
            modifier = modifier.clickable(onClick = onClick),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun HistoryDateButton(
    label: String,
    date: LocalDate,
    formatter: DateTimeFormatter,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium
            )

            Text(
                text = date.format(formatter),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun HistoryDetailRow(
    label: String,
    value: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

private fun showDatePicker(
    context: Context,
    initialDate: LocalDate,
    minimumDate: LocalDate?,
    maximumDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val dialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateSelected(
                LocalDate.of(
                    year,
                    month + 1,
                    dayOfMonth
                )
            )
        },
        initialDate.year,
        initialDate.monthValue - 1,
        initialDate.dayOfMonth
    )

    minimumDate?.let { date ->
        dialog.datePicker.minDate = date
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    dialog.datePicker.maxDate = maximumDate
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    dialog.show()
}
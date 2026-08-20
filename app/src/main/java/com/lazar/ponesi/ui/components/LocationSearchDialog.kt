package com.lazar.ponesi.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lazar.ponesi.R
import com.lazar.ponesi.data.model.LocationSuggestion
import com.lazar.ponesi.ui.theme.Dimens
import com.lazar.ponesi.viewmodel.LocationSearchUiState

@Composable
fun LocationSearchDialog(
    searchState: LocationSearchUiState,
    onQueryChange: (String) -> Unit,
    onLocationSelected: (LocationSuggestion) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(
                    R.string.title_choose_location
                )
            )
        },
        text = {
            Column(
                modifier = Modifier.heightIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(
                    Dimens.SpacingMedium
                )
            ) {
                OutlinedTextField(
                    value = searchState.query,
                    onValueChange = onQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(
                                R.string.label_search_city
                            )
                        )
                    },
                    singleLine = true
                )

                when {
                    searchState.isLoading -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    searchState.hasSearchError -> {
                        Text(
                            text = stringResource(
                                R.string.message_location_search_failed
                            ),
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    searchState.query.trim().length < 2 -> {
                        Text(
                            text = stringResource(
                                R.string.message_location_search_hint
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    searchState.results.isEmpty() -> {
                        Text(
                            text = stringResource(
                                R.string.message_location_not_found
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    else -> {
                        Column(
                            modifier = Modifier
                                .heightIn(max = 280.dp)
                                .verticalScroll(
                                    rememberScrollState()
                                )
                        ) {
                            searchState.results.forEachIndexed {
                                    index,
                                    location ->

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onLocationSelected(location)
                                        }
                                        .padding(
                                            vertical = Dimens.SpacingMedium
                                        ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(
                                        Dimens.SpacingMedium
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null
                                    )

                                    Text(
                                        text = location.displayName,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }

                                if (index != searchState.results.lastIndex) {
                                    HorizontalDivider()
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = stringResource(
                        R.string.action_close
                    )
                )
            }
        }
    )
}
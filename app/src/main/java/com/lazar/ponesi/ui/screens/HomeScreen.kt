package com.lazar.ponesi.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lazar.ponesi.R
import com.lazar.ponesi.data.model.Travel
import com.lazar.ponesi.ui.components.AppTopBar
import com.lazar.ponesi.ui.components.BottomActionButtons
import com.lazar.ponesi.ui.components.TravelCard
import com.lazar.ponesi.ui.theme.Dimens
import com.lazar.ponesi.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    onDocumentsClick: () -> Unit,
    onCreateTravelClick: () -> Unit,
    onEditTravelClick: (Int) -> Unit,
    homeViewModel: HomeViewModel
) {
    val travels by homeViewModel.travels.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    var travelPendingDeletion by remember {
        mutableStateOf<Travel?>(null)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.title_travels),
                showFilter = true,
                showSort = true
            )
        },
        bottomBar = {
            BottomActionButtons(
                onDocumentsClick = onDocumentsClick,
                onCreateTemplateClick = onCreateTravelClick
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(Dimens.SpacingLarge),
            verticalArrangement = Arrangement.spacedBy(
                Dimens.SpacingMedium
            )
        ) {
            items(travels) { travel ->

                TravelCard(
                    travel = travel,

                    onClick = {
                        println("Kliknuto ${travel.name}")
                    },

                    onEditClick = {
                        onEditTravelClick(travel.id)
                    },

                    onDeleteClick = {
                        travelPendingDeletion = travel
                    }
                )
            }
        }
    }

    travelPendingDeletion?.let { travel ->

        AlertDialog(
            onDismissRequest = {
                travelPendingDeletion = null
            },

            title = {
                Text(
                    text = stringResource(
                        R.string.title_delete_template
                    )
                )
            },

            text = {
                Text(
                    text = stringResource(
                        R.string.message_delete_template,
                        travel.name
                    )
                )
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        homeViewModel.deleteTravel(travel.id)
                        travelPendingDeletion = null
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
                        travelPendingDeletion = null
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
}
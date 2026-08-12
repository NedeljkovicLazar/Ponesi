package com.lazar.ponesi.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lazar.ponesi.R
import com.lazar.ponesi.ui.components.AppTopBar
import com.lazar.ponesi.ui.components.ChecklistCategoryCard
import com.lazar.ponesi.ui.theme.Dimens
import com.lazar.ponesi.viewmodel.TravelDetailsViewModel

@Composable
fun TravelDetailsScreen(
    onBackClick: () -> Unit,
    travelDetailsViewModel: TravelDetailsViewModel
) {
    val uiState by travelDetailsViewModel
        .uiState
        .collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AppTopBar(
                title = uiState.travel?.name
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

            uiState.travel == null -> {
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
                val travel = uiState.travel
                    ?: return@Scaffold

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
}
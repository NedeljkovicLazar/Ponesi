package com.lazar.ponesi.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lazar.ponesi.R
import com.lazar.ponesi.ui.components.AppTopBar
import com.lazar.ponesi.ui.components.CategoryCard
import com.lazar.ponesi.ui.components.PrimaryButton
import com.lazar.ponesi.ui.theme.Dimens
import com.lazar.ponesi.viewmodel.CreateTravelViewModel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect

@Composable
fun CreateTravelScreen(
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    createTravelViewModel: CreateTravelViewModel
) {

    val uiState by createTravelViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaveComplete) {
        if (uiState.isSaveComplete) {
            onSaveSuccess()
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.title_new_template),
                showBackButton = true,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(Dimens.SpacingLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge)
        ) {

            item {
                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = { newName ->
                        createTravelViewModel.onNameChange(newName)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(R.string.label_template_name)
                        )
                    },
                    singleLine = true
                )
            }

            item {
                Text(
                    text = stringResource(R.string.label_categories)
                )
            }

            items(uiState.categories) { category ->
                CategoryCard(
                    category = category,

                    onAddItemClick = {
                        createTravelViewModel.showAddItemDialog(category.id)
                    },

                    onRemoveCategoryClick = {
                        createTravelViewModel.removeCategory(category.id)
                    },

                    onRemoveItemClick = { itemId ->
                        createTravelViewModel.removeItem(
                            categoryId = category.id,
                            itemId = itemId
                        )
                    }
                )
            }

            item {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        createTravelViewModel.showAddCategoryDialog()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.action_add_category)
                    )
                }
            }

            item {
                PrimaryButton(
                    text = stringResource(R.string.action_save),
                    onClick = {
                        createTravelViewModel.saveTravel()
                    }
                )
            }
        }
    }
    if (uiState.isAddCategoryDialogVisible) {

        AlertDialog(
            onDismissRequest = {
                createTravelViewModel.hideAddCategoryDialog()
            },

            title = {
                Text(
                    text = stringResource(R.string.title_add_category)
                )
            },

            text = {
                OutlinedTextField(
                    value = uiState.newCategoryName,
                    onValueChange = { newName ->
                        createTravelViewModel.onNewCategoryNameChange(newName)
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.label_category_name)
                        )
                    },
                    singleLine = true
                )
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        createTravelViewModel.addCategory()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.action_add)
                    )
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        createTravelViewModel.hideAddCategoryDialog()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.action_cancel)
                    )
                }
            }
        )
    }
    if (uiState.selectedCategoryId != null) {

        AlertDialog(
            onDismissRequest = {
                createTravelViewModel.hideAddItemDialog()
            },

            title = {
                Text(
                    text = stringResource(R.string.action_add_item)
                )
            },

            text = {
                OutlinedTextField(
                    value = uiState.newItemName,
                    onValueChange = { newName ->
                        createTravelViewModel.onNewItemNameChange(newName)
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.label_item_name)
                        )
                    },
                    singleLine = true
                )
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        createTravelViewModel.addItem()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.action_add)
                    )
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        createTravelViewModel.hideAddItemDialog()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.action_cancel)
                    )
                }
            }
        )
    }
}
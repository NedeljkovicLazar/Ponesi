package com.lazar.ponesi.viewmodel

import com.lazar.ponesi.data.model.Category

data class CreateTravelUiState(
    val name: String = "",
    val categories: List<Category> = emptyList(),

    val isAddCategoryDialogVisible: Boolean = false,
    val newCategoryName: String = "",

    val selectedCategoryId: Int? = null,
    val newItemName: String = "",

    val editingCategoryId: Int? = null,
    val editedCategoryName: String = "",

    val editingItemCategoryId: Int? = null,
    val editingItemId: Int? = null,
    val editedItemName: String = "",

    val isEditMode: Boolean = false,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isSaveComplete: Boolean = false
)
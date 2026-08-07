package com.lazar.ponesi.viewmodel

import com.lazar.ponesi.data.model.Category

data class CreateTravelUiState(
    val name: String = "",
    val categories: List<Category> = emptyList(),

    val isAddCategoryDialogVisible: Boolean = false,
    val newCategoryName: String = "",

    val selectedCategoryId: Int? = null,
    val newItemName: String = ""
)
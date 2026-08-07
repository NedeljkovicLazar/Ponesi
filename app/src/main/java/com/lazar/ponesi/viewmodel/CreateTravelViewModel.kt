package com.lazar.ponesi.viewmodel

import androidx.lifecycle.ViewModel
import com.lazar.ponesi.data.model.Category
import com.lazar.ponesi.data.model.PackingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateTravelViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        CreateTravelUiState(
            categories = listOf(
                Category(
                    id = 1,
                    name = "Kofer",
                    items = listOf(
                        PackingItem(
                            id = 1,
                            name = "Majice"
                        ),
                        PackingItem(
                            id = 2,
                            name = "Pantalone"
                        ),
                        PackingItem(
                            id = 3,
                            name = "Donji veš"
                        )
                    )
                ),
                Category(
                    id = 2,
                    name = "Ranac",
                    items = listOf(
                        PackingItem(
                            id = 4,
                            name = "Punjač"
                        ),
                        PackingItem(
                            id = 5,
                            name = "Slušalice"
                        )
                    )
                ),
                Category(
                    id = 3,
                    name = "Neseser",
                    items = listOf(
                        PackingItem(
                            id = 6,
                            name = "Četkica za zube"
                        ),
                        PackingItem(
                            id = 7,
                            name = "Pasta za zube"
                        )
                    )
                )
            )
        )
    )

    val uiState: StateFlow<CreateTravelUiState> =
        _uiState.asStateFlow()

    fun onNameChange(newName: String) {
        _uiState.value = _uiState.value.copy(
            name = newName
        )
    }

    fun showAddCategoryDialog() {
        _uiState.value = _uiState.value.copy(
            isAddCategoryDialogVisible = true,
            newCategoryName = ""
        )
    }

    fun hideAddCategoryDialog() {
        _uiState.value = _uiState.value.copy(
            isAddCategoryDialogVisible = false,
            newCategoryName = ""
        )
    }

    fun onNewCategoryNameChange(newName: String) {
        _uiState.value = _uiState.value.copy(
            newCategoryName = newName
        )
    }

    fun addCategory() {

        val categoryName = _uiState.value.newCategoryName.trim()

        if (categoryName.isBlank()) {
            return
        }

        val nextId =
            (_uiState.value.categories.maxOfOrNull { it.id } ?: 0) + 1

        val newCategory = Category(
            id = nextId,
            name = categoryName
        )

        _uiState.value = _uiState.value.copy(
            categories = _uiState.value.categories + newCategory,
            isAddCategoryDialogVisible = false,
            newCategoryName = ""
        )
    }
    fun showAddItemDialog(categoryId: Int) {
        _uiState.value = _uiState.value.copy(
            selectedCategoryId = categoryId,
            newItemName = ""
        )
    }

    fun hideAddItemDialog() {
        _uiState.value = _uiState.value.copy(
            selectedCategoryId = null,
            newItemName = ""
        )
    }

    fun onNewItemNameChange(newName: String) {
        _uiState.value = _uiState.value.copy(
            newItemName = newName
        )
    }

    fun addItem() {

        val currentState = _uiState.value

        val categoryId = currentState.selectedCategoryId
            ?: return

        val itemName = currentState.newItemName.trim()

        if (itemName.isBlank()) {
            return
        }

        val nextItemId =
            (currentState.categories
                .flatMap { it.items }
                .maxOfOrNull { it.id } ?: 0) + 1

        val newItem = PackingItem(
            id = nextItemId,
            name = itemName
        )

        val updatedCategories = currentState.categories.map { category ->

            if (category.id == categoryId) {
                category.copy(
                    items = category.items + newItem
                )
            } else {
                category
            }
        }

        _uiState.value = currentState.copy(
            categories = updatedCategories,
            selectedCategoryId = null,
            newItemName = ""
        )
    }
}
package com.lazar.ponesi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazar.ponesi.data.model.Category
import com.lazar.ponesi.data.model.PackingItem
import com.lazar.ponesi.data.repository.TravelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateTravelViewModel(
    private val travelRepository: TravelRepository,
    private val travelId: Int? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CreateTravelUiState(
            isEditMode = travelId != null,
            isLoading = travelId != null
        )
    )

    val uiState: StateFlow<CreateTravelUiState> =
        _uiState.asStateFlow()

    init {
        if (travelId != null) {
            loadTravel(travelId)
        }
    }

    private fun loadTravel(travelId: Int) {
        viewModelScope.launch {

            val travel = travelRepository.getTravel(travelId)

            _uiState.value = if (travel != null) {
                _uiState.value.copy(
                    name = travel.name,
                    categories = travel.categories,
                    isLoading = false
                )
            } else {
                _uiState.value.copy(
                    isLoading = false
                )
            }
        }
    }

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

    fun saveTravel() {

        val currentState = _uiState.value

        val travelName = currentState.name.trim()

        if (
            travelName.isBlank() ||
            currentState.categories.isEmpty() ||
            currentState.isSaving
        ) {
            return
        }

        _uiState.value = currentState.copy(
            isSaving = true
        )

        viewModelScope.launch {

            try {

                if (travelId == null) {
                    travelRepository.saveTravel(
                        name = travelName,
                        categories = currentState.categories
                    )
                } else {
                    travelRepository.updateTravel(
                        travelId = travelId,
                        name = travelName,
                        categories = currentState.categories
                    )
                }

                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    isSaveComplete = true
                )

            } catch (exception: Exception) {

                _uiState.value = _uiState.value.copy(
                    isSaving = false
                )

                exception.printStackTrace()
            }
        }
    }

    fun removeCategory(categoryId: Int) {
        _uiState.value = _uiState.value.copy(
            categories = _uiState.value.categories.filterNot { category ->
                category.id == categoryId
            }
        )
    }

    fun removeItem(
        categoryId: Int,
        itemId: Int
    ) {
        val updatedCategories = _uiState.value.categories.map { category ->

            if (category.id == categoryId) {
                category.copy(
                    items = category.items.filterNot { item ->
                        item.id == itemId
                    }
                )
            } else {
                category
            }
        }

        _uiState.value = _uiState.value.copy(
            categories = updatedCategories
        )
    }
    fun showEditCategoryDialog(categoryId: Int) {

        val category = _uiState.value.categories.find { category ->
            category.id == categoryId
        } ?: return

        _uiState.value = _uiState.value.copy(
            editingCategoryId = categoryId,
            editedCategoryName = category.name
        )
    }

    fun hideEditCategoryDialog() {
        _uiState.value = _uiState.value.copy(
            editingCategoryId = null,
            editedCategoryName = ""
        )
    }

    fun onEditedCategoryNameChange(newName: String) {
        _uiState.value = _uiState.value.copy(
            editedCategoryName = newName
        )
    }

    fun renameCategory() {

        val currentState = _uiState.value

        val categoryId = currentState.editingCategoryId
            ?: return

        val categoryName = currentState.editedCategoryName.trim()

        if (categoryName.isBlank()) {
            return
        }

        val updatedCategories = currentState.categories.map { category ->

            if (category.id == categoryId) {
                category.copy(
                    name = categoryName
                )
            } else {
                category
            }
        }

        _uiState.value = currentState.copy(
            categories = updatedCategories,
            editingCategoryId = null,
            editedCategoryName = ""
        )
    }

    fun showEditItemDialog(
        categoryId: Int,
        itemId: Int
    ) {

        val category = _uiState.value.categories.find { category ->
            category.id == categoryId
        } ?: return

        val item = category.items.find { item ->
            item.id == itemId
        } ?: return

        _uiState.value = _uiState.value.copy(
            editingItemCategoryId = categoryId,
            editingItemId = itemId,
            editedItemName = item.name
        )
    }

    fun hideEditItemDialog() {
        _uiState.value = _uiState.value.copy(
            editingItemCategoryId = null,
            editingItemId = null,
            editedItemName = ""
        )
    }

    fun onEditedItemNameChange(newName: String) {
        _uiState.value = _uiState.value.copy(
            editedItemName = newName
        )
    }

    fun renameItem() {

        val currentState = _uiState.value

        val categoryId = currentState.editingItemCategoryId
            ?: return

        val itemId = currentState.editingItemId
            ?: return

        val itemName = currentState.editedItemName.trim()

        if (itemName.isBlank()) {
            return
        }

        val updatedCategories = currentState.categories.map { category ->

            if (category.id == categoryId) {
                category.copy(
                    items = category.items.map { item ->

                        if (item.id == itemId) {
                            item.copy(
                                name = itemName
                            )
                        } else {
                            item
                        }
                    }
                )
            } else {
                category
            }
        }

        _uiState.value = currentState.copy(
            categories = updatedCategories,
            editingItemCategoryId = null,
            editingItemId = null,
            editedItemName = ""
        )
    }
}
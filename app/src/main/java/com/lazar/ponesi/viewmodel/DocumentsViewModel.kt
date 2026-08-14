package com.lazar.ponesi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazar.ponesi.data.model.SavedDocument
import com.lazar.ponesi.data.repository.DocumentRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DocumentsViewModel(
    private val documentRepository: DocumentRepository
) : ViewModel() {

    val documents: StateFlow<List<SavedDocument>> =
        documentRepository
            .getAllDocuments()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun addDocument(
        title: String,
        uri: String,
        mimeType: String
    ) {
        if (title.isBlank()) {
            return
        }

        viewModelScope.launch {
            documentRepository.addDocument(
                title = title,
                uri = uri,
                mimeType = mimeType
            )
        }
    }

    fun deleteDocument(
        documentId: Int
    ) {
        viewModelScope.launch {
            documentRepository.deleteDocument(documentId)
        }
    }
}
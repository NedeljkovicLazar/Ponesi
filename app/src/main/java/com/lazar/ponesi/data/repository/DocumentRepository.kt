package com.lazar.ponesi.data.repository

import com.lazar.ponesi.data.database.AppDatabase
import com.lazar.ponesi.data.database.entity.DocumentEntity
import com.lazar.ponesi.data.model.SavedDocument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DocumentRepository(
    database: AppDatabase
) {
    private val documentDao = database.documentDao()

    fun getAllDocuments(): Flow<List<SavedDocument>> {
        return documentDao
            .getAllDocuments()
            .map { documents ->
                documents.map { document ->
                    SavedDocument(
                        id = document.id,
                        title = document.title,
                        uri = document.uri,
                        mimeType = document.mimeType
                    )
                }
            }
    }

    suspend fun addDocument(
        title: String,
        uri: String,
        mimeType: String
    ) {
        documentDao.insertDocument(
            DocumentEntity(
                title = title.trim(),
                uri = uri,
                mimeType = mimeType
            )
        )
    }

    suspend fun deleteDocument(
        documentId: Int
    ) {
        documentDao.deleteDocument(documentId)
    }
}
package com.lazar.ponesi.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lazar.ponesi.data.database.entity.DocumentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {

    @Query("SELECT * FROM documents ORDER BY id DESC")
    fun getAllDocuments(): Flow<List<DocumentEntity>>

    @Insert
    suspend fun insertDocument(
        document: DocumentEntity
    )

    @Query("DELETE FROM documents WHERE id = :documentId")
    suspend fun deleteDocument(
        documentId: Int
    )
}
package com.lazar.ponesi.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "documents"
)
data class DocumentEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    val uri: String,

    val mimeType: String
)
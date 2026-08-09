package com.lazar.ponesi.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "packing_items",

    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],

    indices = [
        Index(value = ["categoryId"])
    ]
)
data class PackingItemEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val categoryId: Int,

    val name: String,

    val isChecked: Boolean = false,

    val position: Int
)
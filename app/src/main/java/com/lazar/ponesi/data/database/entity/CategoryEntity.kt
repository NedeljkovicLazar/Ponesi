package com.lazar.ponesi.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",

    foreignKeys = [
        ForeignKey(
            entity = TravelEntity::class,
            parentColumns = ["id"],
            childColumns = ["travelId"],
            onDelete = ForeignKey.CASCADE
        )
    ],

    indices = [
        Index(value = ["travelId"])
    ]
)
data class CategoryEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val travelId: Int,

    val name: String,

    val position: Int
)
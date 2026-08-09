package com.lazar.ponesi.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.lazar.ponesi.data.database.entity.CategoryEntity
import com.lazar.ponesi.data.database.entity.PackingItemEntity

data class CategoryWithItems(

    @Embedded
    val category: CategoryEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val items: List<PackingItemEntity>
)
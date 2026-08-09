package com.lazar.ponesi.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.lazar.ponesi.data.database.entity.CategoryEntity
import com.lazar.ponesi.data.database.entity.TravelEntity

data class TravelWithCategories(

    @Embedded
    val travel: TravelEntity,

    @Relation(
        entity = CategoryEntity::class,
        parentColumn = "id",
        entityColumn = "travelId"
    )
    val categories: List<CategoryWithItems>
)
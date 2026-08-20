package com.lazar.ponesi.data.mapper

import com.lazar.ponesi.data.database.relation.TravelWithCategories
import com.lazar.ponesi.data.model.Category
import com.lazar.ponesi.data.model.PackingItem
import com.lazar.ponesi.data.model.Travel
import com.lazar.ponesi.data.model.TravelLocation

fun TravelWithCategories.toTravel(): Travel {
    return Travel(
        id = travel.id,
        name = travel.name,
        status = travel.status,

        categories = categories
            .sortedBy { it.category.position }
            .map { categoryWithItems ->

                Category(
                    id = categoryWithItems.category.id,
                    name = categoryWithItems.category.name,

                    items = categoryWithItems.items
                        .sortedBy { it.position }
                        .map { itemEntity ->
                            PackingItem(
                                id = itemEntity.id,
                                name = itemEntity.name,
                                isChecked = itemEntity.isChecked
                            )
                        }
                )
            },

        date = travel.date,
        startDate = travel.startDate,
        location = if (
            travel.locationName != null &&
            travel.locationLatitude != null &&
            travel.locationLongitude != null
        ) {
            TravelLocation(
                name = travel.locationName,
                latitude = travel.locationLatitude,
                longitude = travel.locationLongitude
            )
        } else {
            null
        }
    )
}
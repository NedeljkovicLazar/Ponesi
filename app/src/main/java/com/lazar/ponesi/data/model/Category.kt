package com.lazar.ponesi.data.model

data class Category(
    val id: Int,
    val name: String,
    val items: List<PackingItem>
)
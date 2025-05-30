package com.example.fooddelivery.data

import androidx.room.Entity

@Entity(tableName = "products")
data class Product(
    val id: Int = 0,
    val name: String = "",
    val price: Double = 0.0,
    val images: List<String?> = emptyList(),
    val category: String = "",
    val subcategory: String = "",
    val description: String = "",
    val attributes: Map<String, String> = emptyMap(),
    var isFavorite: Boolean = false
)

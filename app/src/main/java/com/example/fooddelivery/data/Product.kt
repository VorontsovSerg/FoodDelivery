package com.example.fooddelivery.data

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val imageUrl: String?,
    val category: String,
    val subcategory: String,
    val discount: Double? = null,
    var isFavorite: Boolean = false
)
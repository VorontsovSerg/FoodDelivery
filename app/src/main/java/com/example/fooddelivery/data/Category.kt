package com.example.fooddelivery.data

data class Category(
    val name: String,
    val subcategories: List<Subcategory>,
    val gradient: List<Long> // цвета градиента в формате 0xFF...
)

data class Subcategory(
    val name: String,
    val color: Long // цвет карточки
)
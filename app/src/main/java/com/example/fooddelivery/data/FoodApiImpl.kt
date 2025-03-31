package com.example.fooddelivery.data

import android.content.Context

class FoodApiImpl(private val context: Context) : FoodApi {
    override suspend fun getNewProducts(): List<Product> {
        return listOf(
            Product(
                id = 1,
                name = "Яблоко",
                price = 50.0,
                images = listOf("apple.jpg"),
                category = "Базар",
                subcategory = "Овощи и фрукты",
                description = "Свежие яблоки",
                attributes = mapOf("Вес" to "200г"),
                isFavorite = false
            ),
            Product(
                id = 2,
                name = "Банан",
                price = 30.0,
                images = listOf("banana.jpg"),
                category = "Базар",
                subcategory = "Овощи и фрукты",
                description = "Спелые бананы",
                attributes = mapOf("Вес" to "150г"),
                isFavorite = false
            )
        )
    }

    override suspend fun getRecommendedProducts(): List<Product> {
        return listOf(
            Product(
                id = 3,
                name = "Говядина",
                price = 300.0,
                images = listOf("beef.jpg"),
                category = "Базар",
                subcategory = "Мясо",
                description = "Свежее мясо",
                attributes = mapOf("Вес" to "500г"),
                isFavorite = false
            ),
            Product(
                id = 4,
                name = "Лосось",
                price = 400.0,
                images = listOf("salmon.jpg"),
                category = "Базар",
                subcategory = "Рыба",
                description = "Свежая рыба",
                attributes = mapOf("Вес" to "300г"),
                isFavorite = false
            )
        )
    }

    override suspend fun getCategories(): List<Category> {
        return listOf(
            Category(
                name = "Базар",
                subcategories = listOf(
                    Subcategory(name = "Овощи и фрукты", color = 0xFFFFA500), // Оранжевый
                    Subcategory(name = "Мясо", color = 0xFFFF0000),         // Красный
                    Subcategory(name = "Рыба", color = 0xFF00FFFF)          // Бирюзовый
                ),
                gradient = listOf(0xFFFD9045, 0xFFF65C51)
            )
        )
    }

    override suspend fun getProductsBySubcategory(categoryName: String, subcategoryName: String): List<Product> {
        val allProducts = getNewProducts() + getRecommendedProducts()
        return allProducts.filter { it.category == categoryName && it.subcategory == subcategoryName }
    }

    override suspend fun searchProducts(query: String): List<Product> {
        val allProducts = getNewProducts() + getRecommendedProducts()
        return allProducts.filter { it.name.contains(query, ignoreCase = true) }
    }

    override suspend fun getProducts(): List<Product> {
        return getNewProducts() + getRecommendedProducts()
    }
}
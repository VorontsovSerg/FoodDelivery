package com.example.fooddelivery.data

interface ProductApi {
    suspend fun getNewProducts(): List<Product>
    suspend fun getRecommendedProducts(): List<Product>
    suspend fun getCategories(): List<Category>
    suspend fun getProductsBySubcategory(categoryName: String, subcategoryName: String): List<Product>
    suspend fun searchProducts(query: String): List<Product>
    suspend fun getProducts(): List<Product>
}

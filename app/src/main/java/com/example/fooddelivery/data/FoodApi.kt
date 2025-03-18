package com.example.fooddelivery.data

import retrofit2.http.GET
import retrofit2.http.Query

interface FoodApi {
    @GET("products")
    suspend fun getProducts(): List<Product>

    @GET("categories")
    suspend fun getCategories(): List<Category>

    @GET("search")
    suspend fun searchProducts(@Query("query") query: String): List<Product>
}
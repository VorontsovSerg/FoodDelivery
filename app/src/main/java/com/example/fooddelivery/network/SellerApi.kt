package com.example.fooddelivery.network

import com.example.fooddelivery.SellerData
import com.example.fooddelivery.data.Product

interface SellerApi {
    suspend fun getSeller(userId: String): SellerData?
    suspend fun registerSeller(seller: SellerData)
    suspend fun getProducts(): List<Product>
    suspend fun addProduct(product: Product)
    suspend fun updateProduct(productId: String, product: Product)
    suspend fun deleteProduct(productId: String)
}
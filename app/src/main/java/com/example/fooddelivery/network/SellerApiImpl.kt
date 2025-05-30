package com.example.fooddelivery.network

import com.example.fooddelivery.SellerData
import com.example.fooddelivery.data.Product
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

class SellerApiImpl(private val client: HttpClient) : SellerApi {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getSeller(userId: String): SellerData? {
        return try {
            val response = client.get("http://your-api-url/sellers/$userId") {
                header("Authorization", "Bearer $userId")
            }
            json.decodeFromString(SellerData.serializer(), response.bodyAsText())
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun registerSeller(seller: SellerData) {
        client.post("http://your-api-url/sellers") {
            contentType(ContentType.Application.Json)
            setBody(seller)
        }
    }

    override suspend fun getProducts(): List<Product> {
        val response = client.get("http://your-api-url/products")
        return json.decodeFromString(response.bodyAsText())
    }

    override suspend fun addProduct(product: Product) {
        client.post("http://your-api-url/products") {
            contentType(ContentType.Application.Json)
            setBody(product)
        }
    }

    override suspend fun updateProduct(productId: String, product: Product) {
        client.put("http://your-api-url/products/$productId") {
            contentType(ContentType.Application.Json)
            setBody(product)
        }
    }

    override suspend fun deleteProduct(productId: String) {
        client.delete("http://your-api-url/products/$productId")
    }
}
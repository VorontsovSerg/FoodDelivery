package com.example.fooddelivery.auth

import com.example.fooddelivery.network.ApiClient
import com.example.fooddelivery.network.LoginRequest
import com.example.fooddelivery.network.RegisterRequest

class AuthRepository {
    suspend fun login(login: String, password: String): String {
        val response = ApiClient.authApi.login(LoginRequest(login, password))
        return response.token
    }

    suspend fun register(login: String, password: String, email: String): String {
        val response = ApiClient.authApi.register(RegisterRequest(login, password, email))
        return response.token
    }
}
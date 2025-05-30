package com.example.fooddelivery.network

import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val login: String, val password: String)
data class RegisterRequest(val login: String, val password: String, val email: String)
data class AuthResponse(val token: String)

interface AuthApi {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
}
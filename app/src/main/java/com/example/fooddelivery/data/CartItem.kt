package com.example.fooddelivery.data
import androidx.room.Entity
import androidx.room.PrimaryKey

data class CartItem(
    @PrimaryKey val id: Int,
    val title: String,
    val price: Int,
    val image: String,
    var quantity: Int
)

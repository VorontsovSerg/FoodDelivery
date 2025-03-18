package com.example.fooddelivery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddelivery.data.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CartItem(val product: Product, var quantity: Int)

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    val totalPrice: StateFlow<Double> = MutableStateFlow(0.0).apply {
        viewModelScope.launch {
            _cartItems.collect { items ->
                value = items.sumOf { it.product.price * it.quantity }
            }
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            val currentItems = _cartItems.value.toMutableList()
            val existingItem = currentItems.find { it.product.id == product.id }
            if (existingItem != null) {
                existingItem.quantity++
            } else {
                currentItems.add(CartItem(product, 1))
            }
            _cartItems.value = currentItems
        }
    }

    fun removeFromCart(productId: Int) {
        viewModelScope.launch {
            _cartItems.value = _cartItems.value.filter { it.product.id != productId }
        }
    }

    fun updateQuantity(productId: Int, delta: Int) {
        viewModelScope.launch {
            val currentItems = _cartItems.value.toMutableList()
            val item = currentItems.find { it.product.id == productId }
            if (item != null) {
                item.quantity = (item.quantity + delta).coerceAtLeast(0)
                if (item.quantity == 0) {
                    currentItems.remove(item)
                }
                _cartItems.value = currentItems
            }
        }
    }
}
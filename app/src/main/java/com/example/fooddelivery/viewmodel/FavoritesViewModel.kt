package com.example.fooddelivery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddelivery.data.FoodApi
import com.example.fooddelivery.data.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(private val api: FoodApi) : ViewModel() {
    private val _favoriteProducts = MutableStateFlow<List<Product>>(emptyList())
    val favoriteProducts: StateFlow<List<Product>> = _favoriteProducts

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            try {
                _favoriteProducts.value = api.getProducts().filter { it.isFavorite }
            } catch (e: Exception) {
                // Обработка ошибок
            }
        }
    }

    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            val updatedFavorites = _favoriteProducts.value.toMutableList()
            val existingProduct = updatedFavorites.find { it.id == product.id }
            if (existingProduct != null) {
                updatedFavorites.remove(existingProduct)
            } else {
                updatedFavorites.add(product.copy(isFavorite = true))
            }
            _favoriteProducts.value = updatedFavorites
        }
    }
}
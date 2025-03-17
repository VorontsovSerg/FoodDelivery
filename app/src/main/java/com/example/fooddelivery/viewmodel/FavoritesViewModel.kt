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
                val products = api.getProducts()
                _favoriteProducts.value = products.filter { it.isFavorite }
            } catch (e: Exception) {
                // Обработка ошибок
            }
        }
    }

    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            val updatedList = _favoriteProducts.value.toMutableList()
            val index = updatedList.indexOfFirst { it.id == product.id }
            if (index != -1) {
                updatedList[index] = updatedList[index].copy(isFavorite = !updatedList[index].isFavorite)
                _favoriteProducts.value = updatedList.filter { it.isFavorite }
            }
        }
    }
}
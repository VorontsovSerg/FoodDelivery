package com.example.fooddelivery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddelivery.data.FoodApi
import com.example.fooddelivery.data.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val api: FoodApi) : ViewModel() {
    private val _newProducts = MutableStateFlow<List<Product>>(emptyList())
    val newProducts: StateFlow<List<Product>> = _newProducts

    private val _recommendedProducts = MutableStateFlow<List<Product>>(emptyList())
    val recommendedProducts: StateFlow<List<Product>> = _recommendedProducts

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            try {
                val products = api.getProducts()
                _newProducts.value = products.take(5) // Например, первые 5 как новинки
                _recommendedProducts.value = products.drop(5).take(5) // Следующие 5 как рекомендации
            } catch (e: Exception) {
                // Обработка ошибок
            }
        }
    }
}
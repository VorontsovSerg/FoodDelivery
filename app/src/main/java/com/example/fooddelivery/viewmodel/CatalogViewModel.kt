package com.example.fooddelivery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddelivery.data.Category
import com.example.fooddelivery.data.FoodApi
import com.example.fooddelivery.data.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatalogViewModel(private val api: FoodApi) : ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _subcategoryProducts = MutableStateFlow<List<Product>>(emptyList())
    val subcategoryProducts: StateFlow<List<Product>> = _subcategoryProducts

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                _categories.value = api.getCategories()
            } catch (e: Exception) {
                // Обработка ошибок
            }
        }
    }

    fun loadProductsForSubcategory(category: String, subcategory: String) {
        viewModelScope.launch {
            try {
                val products = api.getProducts()
                _subcategoryProducts.value = products.filter {
                    it.category == category && it.subcategory == subcategory
                }
            } catch (e: Exception) {
                // Обработка ошибок
            }
        }
    }
}
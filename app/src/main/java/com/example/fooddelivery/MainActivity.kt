package com.example.fooddelivery

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fooddelivery.ui.components.BottomNavigationBar
import com.example.fooddelivery.ui.components.SearchBar
import com.example.fooddelivery.ui.screens.*
import com.example.fooddelivery.viewmodel.*
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalFocusManager
import com.example.fooddelivery.data.FoodApiImpl

@Composable
fun FoodDeliveryApp() {
    val navController = rememberNavController()
    var searchQuery by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isSearchFocused by remember { mutableStateOf(false) } // Состояние фокуса
    val focusManager = LocalFocusManager.current // Для управления фокусом

    val api = FoodApiImpl(context)
    val mainViewModel = MainViewModel(api)
    val catalogViewModel = CatalogViewModel(api)
    val favoritesViewModel = FavoritesViewModel(context)
    val cartViewModel = CartViewModel(context)
    val searchViewModel = SearchViewModel(api)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            SearchBar(
                searchQuery = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { query ->
                    scope.launch {
                        searchViewModel.searchProducts(query)
                        if (query.isNotEmpty()) navController.navigate("search")
                    }
                },
                onFocusChange = { focused ->
                    isSearchFocused = focused
                }
            )
            Box(modifier = Modifier.weight(1f)) {
                val newProducts by mainViewModel.newProducts.collectAsState()
                val recommendedProducts by mainViewModel.recommendedProducts.collectAsState()
                val categories by catalogViewModel.categories.collectAsState()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { HomeScreen(mainViewModel, navController, favoritesViewModel) }
                    composable("catalog") { CatalogScreen(catalogViewModel, navController) }
                    composable("favorites") { FavoritesScreen(favoritesViewModel) }
                    composable("cart") { CartScreen(cartViewModel) }
                    composable("search") { SearchScreen(searchViewModel, searchQuery) }
                    composable("subcategories/{categoryName}") { backStackEntry ->
                        val categoryName = backStackEntry.arguments?.getString("categoryName")
                        val category = categories.find { it.name == categoryName }
                        category?.let { SubcategoryScreen(it, navController) }
                            ?: Text("Категория не найдена")
                    }
                    composable("products/{categoryName}/{subcategoryName}") { backStackEntry ->
                        val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
                        val subcategoryName = backStackEntry.arguments?.getString("subcategoryName") ?: ""
                        ProductsBySubcategoryScreen(catalogViewModel, categoryName, subcategoryName, navController)
                    }
                    composable("product/{productId}") { backStackEntry ->
                        val productId = backStackEntry.arguments?.getString("productId")?.toInt()
                        val product = newProducts.find { it.id == productId }
                            ?: recommendedProducts.find { it.id == productId }
                        product?.let { ProductDetailScreen(it, cartViewModel, favoritesViewModel) }
                            ?: Text("Товар не найден")
                    }
                }
            }
            BottomNavigationBar(navController)
        }

        // Черный полупрозрачный квадрат при фокусе
        if (isSearchFocused) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)) // Полупрозрачный черный
                    .offset(y = 64.dp) // Смещение ниже строки поиска (примерная высота SearchBar)
                    .clickable(
                        onClick = {
                            isSearchFocused = false
                            focusManager.clearFocus() // Сбрасываем фокус при клике на затемнение
                        },
                        indication = null, // Убираем эффект нажатия
                        interactionSource = remember { MutableInteractionSource() }
                    )
            )
        }
    }

    // Обработка кнопки "Назад"
    BackHandler(enabled = isSearchFocused) {
        isSearchFocused = false
        focusManager.clearFocus() // Сбрасываем фокус и убираем затемнение
    }
}
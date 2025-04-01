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
import com.example.fooddelivery.data.FoodApiImpl
import com.example.fooddelivery.ui.components.BottomNavigationBar
import com.example.fooddelivery.ui.components.SearchBar
import com.example.fooddelivery.ui.screens.*
import com.example.fooddelivery.viewmodel.*
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun FoodDeliveryApp() {
    val navController = rememberNavController()
    var searchQuery by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isSearchFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

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

            // Затемнение под SearchBar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Основной контент (NavHost)
                val newProducts by mainViewModel.newProducts.collectAsState()
                val recommendedProducts by mainViewModel.recommendedProducts.collectAsState()
                val categories by catalogViewModel.categories.collectAsState()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { HomeScreen(mainViewModel, navController, favoritesViewModel) }
                    composable("catalog") { CatalogScreen(catalogViewModel, navController) }
                    composable("favorites") { FavoritesScreen(favoritesViewModel, navController) }
                    composable("cart") { CartScreen(cartViewModel) }
                    composable("search") { SearchScreen(searchViewModel, searchQuery, navController) }
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

                // Затемнение поверх NavHost, но под SearchBar
                if (isSearchFocused) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                            .clickable(
                                onClick = {
                                    isSearchFocused = false
                                    focusManager.clearFocus()
                                },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            )
                    )
                }
            }

            BottomNavigationBar(navController)
        }
    }

    BackHandler(enabled = isSearchFocused) {
        isSearchFocused = false
        focusManager.clearFocus()
    }
}
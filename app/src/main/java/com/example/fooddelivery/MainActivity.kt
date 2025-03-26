package com.example.fooddelivery

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import com.example.fooddelivery.data.*
import com.example.fooddelivery.ui.components.BottomNavigationBar
import com.example.fooddelivery.ui.components.SearchBar
import com.example.fooddelivery.ui.screens.*
import com.example.fooddelivery.ui.theme.FoodDeliveryTheme
import com.example.fooddelivery.viewmodel.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodDeliveryTheme {
                FoodDeliveryApp()
            }
        }
    }
}

@Composable
fun FoodDeliveryApp() {
    val navController = rememberNavController()
    var searchQuery by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val api = FoodApiImpl(context)
    val mainViewModel = MainViewModel(api)
    val catalogViewModel = CatalogViewModel(api)
    val favoritesViewModel = FavoritesViewModel(api)
    val cartViewModel = CartViewModel(context)
    val searchViewModel = SearchViewModel(api)

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
            }
        )
        Box(modifier = Modifier.weight(1f)) {
            val newProducts by mainViewModel.newProducts.collectAsState()
            val recommendedProducts by mainViewModel.recommendedProducts.collectAsState()
            val categories by catalogViewModel.categories.collectAsState()

            NavHost(navController = navController, startDestination = "home") {
                composable("home") { HomeScreen(mainViewModel, navController) }
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
}

class FoodApiImpl(private val context: Context) : FoodApi {
    private val _products = MutableStateFlow(FoodData.products.toMutableList())

    init {
        loadFavorites()
    }

    override suspend fun getProducts(): List<Product> = _products.value
    override suspend fun getCategories(): List<Category> = FoodData.categories
    override suspend fun searchProducts(query: String): List<Product> =
        _products.value.filter { it.name.contains(query, ignoreCase = true) }

    fun toggleFavorite(productId: Int) {
        val updatedProducts = _products.value.map {
            if (it.id == productId) it.copy(isFavorite = !it.isFavorite) else it
        }.toMutableList() // Преобразуем в MutableList
        _products.value = updatedProducts
        Persistence.saveFavorites(context, updatedProducts.filter { it.isFavorite }.map { it.id }.toSet())
    }

    private fun loadFavorites() {
        val favoriteIds = Persistence.loadFavorites(context)
        val updatedProducts = _products.value.map {
            it.copy(isFavorite = it.id in favoriteIds)
        }.toMutableList() // Преобразуем в MutableList
        _products.value = updatedProducts
    }
}
package com.example.fooddelivery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    val api = FoodApiImpl()
    val mainViewModel = MainViewModel(api)
    val catalogViewModel = CatalogViewModel(api)
    val favoritesViewModel = FavoritesViewModel(api)
    val cartViewModel = CartViewModel()
    val searchViewModel = SearchViewModel(api)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        SearchBar { query ->
            searchQuery = query
            scope.launch {
                searchViewModel.searchProducts(query)
                if (query.isNotEmpty()) navController.navigate("search")
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            val newProducts by mainViewModel.newProducts.collectAsState()
            val recommendedProducts by mainViewModel.recommendedProducts.collectAsState()
            val categories by catalogViewModel.categories.collectAsState()

            NavHost(navController = navController, startDestination = "home") {
                composable("home") { HomeScreen(mainViewModel) }
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

// Моковая реализация API остается без изменений
class FoodApiImpl : FoodApi {
    private val products = listOf(
        Product(1, "Яблоко", 10.0, "https://example.com/apple.jpg", "Базар", "Фрукты"),
        Product(2, "Груша", 12.0, "https://example.com/pear.jpg", "Базар", "Фрукты"),
        Product(3, "Банан", 15.0, "https://example.com/banana.jpg", "Базар", "Фрукты"),
        Product(4, "Апельсин", 18.0, "https://example.com/orange.jpg", "Базар", "Фрукты"),
        Product(5, "Киви", 20.0, "https://example.com/kiwi.jpg", "Базар", "Фрукты"),
        Product(6, "Морковь", 8.0, "https://example.com/carrot.jpg", "Базар", "Овощи"),
        Product(7, "Картофель", 5.0, "https://example.com/potato.jpg", "Базар", "Овощи"),
        Product(8, "Лук", 6.0, "https://example.com/onion.jpg", "Базар", "Овощи"),
        Product(9, "Томат", 12.0, "https://example.com/tomato.jpg", "Базар", "Овощи"),
        Product(10, "Огурец", 10.0, null, "Базар", "Овощи")
    )

    private val categories = listOf(
        Category(
            "Базар",
            listOf(
                Subcategory("Фрукты", 0xFF4CAF50),
                Subcategory("Овощи", 0xFF8BC34A)
            ),
            listOf(0xFF4CAF50, 0xFF8BC34A)
        )
    )

    override suspend fun getProducts(): List<Product> = products
    override suspend fun getCategories(): List<Category> = categories
    override suspend fun searchProducts(query: String): List<Product> =
        products.filter { it.name.contains(query, ignoreCase = true) }
}
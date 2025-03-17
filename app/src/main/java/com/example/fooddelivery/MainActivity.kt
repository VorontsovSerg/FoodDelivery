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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fooddelivery.data.Category
import com.example.fooddelivery.data.FoodApi
import com.example.fooddelivery.data.Product
import com.example.fooddelivery.ui.screens.*
import com.example.fooddelivery.ui.theme.FoodDeliveryTheme
import com.example.fooddelivery.viewmodel.*

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

    // Моковые ViewModel для примера (в реальном проекте используйте DI)
    val mainViewModel = MainViewModel(FoodApiImpl())
    val catalogViewModel = CatalogViewModel(FoodApiImpl())
    val favoritesViewModel = FavoritesViewModel(FoodApiImpl())
    val cartViewModel = CartViewModel()
    val searchViewModel = SearchViewModel(FoodApiImpl())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Search Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFEB3B)) // Желтый фон
                .padding(16.dp)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    searchViewModel.searchProducts(it)
                    if (it.isNotEmpty()) navController.navigate("search")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = MaterialTheme.shapes.small)
                    .height(48.dp),
                placeholder = { Text("Поиск товаров") },
                singleLine = true
            )
        }

        // Content Area
        Box(modifier = Modifier.weight(1f)) {
            val newProducts by mainViewModel.newProducts.collectAsState()
            val recommendedProducts by mainViewModel.recommendedProducts.collectAsState()

            NavHost(navController = navController, startDestination = "home") {
                composable("home") { HomeScreen(mainViewModel) }
                composable("catalog") { CatalogScreen(catalogViewModel) }
                composable("favorites") { FavoritesScreen(favoritesViewModel) }
                composable("cart") { CartScreen(cartViewModel) }
                composable("search") { SearchScreen(searchViewModel, searchQuery) }
                composable("product/{productId}") { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId")?.toInt()
                    val product = newProducts.find { it.id == productId }
                        ?: recommendedProducts.find { it.id == productId }
                    product?.let { ProductDetailScreen(it, cartViewModel, favoritesViewModel) }
                        ?: Text("Товар не найден") // Обработка случая, если продукт не найден
                }
            }
        }

        // Bottom Navigation
        BottomNavigationBar(navController)
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFEEEEEE))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        val routes = listOf("home", "catalog", "favorites", "cart", "profile")
        routes.forEach { route ->
            Button(
                onClick = {
                    if (route == "profile") {
                        navController.context.startActivity(
                            android.content.Intent(navController.context, ProfileActivity::class.java)
                        )
                    } else {
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            ) {
                Text(route.replaceFirstChar { it.uppercase() })
            }
        }
    }
}

class FoodApiImpl : FoodApi {
    override suspend fun getProducts(): List<Product> = emptyList()
    override suspend fun getCategories(): List<Category> = emptyList()
    override suspend fun searchProducts(query: String): List<Product> = emptyList()
}
package com.example.fooddelivery.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fooddelivery.data.FoodData
import com.example.fooddelivery.ui.components.ProductCard
import com.example.fooddelivery.viewmodel.FavoritesViewModel
import com.example.fooddelivery.viewmodel.MainViewModel

@Composable
fun HomeScreen(viewModel: MainViewModel, navController: NavController, favoritesViewModel: FavoritesViewModel) {
    val newProducts = viewModel.newProducts.collectAsState().value
    val recommendedProducts = viewModel.recommendedProducts.collectAsState().value
    val subcategories = FoodData.categories.flatMap { it.subcategories } // Собираем все подкатегории

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text("Новинки", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            modifier = Modifier.wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(newProducts) { product ->
                Box(
                    modifier = Modifier
                        .size(width = 150.dp, height = 200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ProductCard(
                        product = product,
                        subcategories = subcategories,
                        onFavoriteClick = { favoritesViewModel.toggleFavorite(it) },
                        onClick = { navController.navigate("product/${product.id}") }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Рекомендуем", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            modifier = Modifier.wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(recommendedProducts) { product ->
                Box(
                    modifier = Modifier
                        .size(width = 150.dp, height = 200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ProductCard(
                        product = product,
                        subcategories = subcategories,
                        onFavoriteClick = { favoritesViewModel.toggleFavorite(it) },
                        onClick = { navController.navigate("product/${product.id}") }
                    )
                }
            }
        }
    }
}
package com.example.fooddelivery.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fooddelivery.ui.components.ProductCard
import com.example.fooddelivery.viewmodel.CatalogViewModel

@Composable
fun ProductsBySubcategoryScreen(
    viewModel: CatalogViewModel,
    categoryName: String,
    subcategoryName: String,
    navController: NavController
) {
    val subcategoryProductsState = viewModel.subcategoryProducts.collectAsState()
    val subcategoryProducts = subcategoryProductsState.value

    // Загружаем товары для подкатегории
    LaunchedEffect(categoryName, subcategoryName) {
        viewModel.loadProductsForSubcategory(categoryName, subcategoryName)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            "Категория: $categoryName - $subcategoryName",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(subcategoryProducts) { product ->
                ProductCard(
                    product = product,
                    onFavoriteClick = { /* Обработка избранного в будущем */ },
                    onClick = { navController.navigate("product/${product.id}") }
                )
            }
        }
    }
}
package com.example.fooddelivery.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fooddelivery.ui.components.ProductCard
import com.example.fooddelivery.viewmodel.MainViewModel

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val newProductsState = viewModel.newProducts.collectAsState()
    val recommendedProductsState = viewModel.recommendedProducts.collectAsState()
    val newProducts = newProductsState.value
    val recommendedProducts = recommendedProductsState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text("Новинки", style = MaterialTheme.typography.headlineSmall)
        LazyRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(newProducts.size) { index ->
                ProductCard(
                    product = newProducts[index],
                    onFavoriteClick = { /* TODO */ },
                    onClick = { /* TODO */ }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Рекомендации", style = MaterialTheme.typography.headlineSmall)
        LazyRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(recommendedProducts.size) { index ->
                ProductCard(
                    product = recommendedProducts[index],
                    onFavoriteClick = { /* TODO */ },
                    onClick = { /* TODO */ }
                )
            }
        }
    }
}
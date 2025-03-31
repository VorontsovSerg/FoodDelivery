package com.example.fooddelivery.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fooddelivery.data.FoodData
import com.example.fooddelivery.ui.components.ProductCard
import com.example.fooddelivery.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(viewModel: SearchViewModel, query: String) {
    val searchResults = viewModel.searchResults.collectAsState().value
    val searchError = viewModel.searchError.collectAsState().value
    val scope = rememberCoroutineScope()
    var lastQuery by remember { mutableStateOf(query) }
    val subcategories = FoodData.categories.flatMap { it.subcategories } // Получаем подкатегории из FoodData

    LaunchedEffect(query) {
        if (query.isNotEmpty()) {
            lastQuery = query
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text("Результаты поиска для \"$query\"", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        when {
            searchError != null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Ошибка поиска: $searchError",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.searchProducts(lastQuery)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Обновить", color = Color.White)
                    }
                }
            }
            searchResults.isEmpty() && query.isNotEmpty() -> {
                Text(
                    "Ничего не найдено",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(searchResults) { product ->
                        Box(
                            modifier = Modifier
                                .size(width = 150.dp, height = 200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ProductCard(
                                product = product,
                                subcategories = subcategories, // Передаем подкатегории
                                onFavoriteClick = { /* Не используется здесь */ },
                                onClick = { /* Навигация не указана */ }
                            )
                        }
                    }
                }
            }
        }
    }
}
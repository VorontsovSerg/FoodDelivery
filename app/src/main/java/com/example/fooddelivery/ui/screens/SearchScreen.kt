package com.example.fooddelivery.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fooddelivery.ui.components.ProductCard
import com.example.fooddelivery.viewmodel.SearchViewModel

@Composable
fun SearchScreen(viewModel: SearchViewModel, query: String) {
    val searchResultsState = viewModel.searchResults.collectAsState()
    val searchResults = searchResultsState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text("Найдено товаров: ${searchResults.size}", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            items(searchResults.size) { index ->
                ProductCard(
                    product = searchResults[index],
                    onFavoriteClick = { /* TODO */ },
                    onClick = { /* TODO */ }
                )
            }
        }
    }
}
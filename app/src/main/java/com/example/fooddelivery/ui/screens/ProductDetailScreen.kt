package com.example.fooddelivery.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.fooddelivery.data.Product
import com.example.fooddelivery.viewmodel.CartViewModel
import com.example.fooddelivery.viewmodel.FavoritesViewModel

@Composable
fun ProductDetailScreen(
    product: Product,
    cartViewModel: CartViewModel,
    favoritesViewModel: FavoritesViewModel
) {
    var isFavorite by remember { mutableStateOf(product.isFavorite) }
    var cartCount by remember { mutableStateOf(cartViewModel.cartItems.value.find { it.product.id == product.id }?.quantity ?: 0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.White, shape = MaterialTheme.shapes.medium)
        ) {
            AsyncImage(
                model = product.imageUrl ?: "error_image_url",
                contentDescription = product.name,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(product.name, style = MaterialTheme.typography.headlineMedium)
        Text("${product.price} ₽", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                isFavorite = !isFavorite
                favoritesViewModel.toggleFavorite(product.copy(isFavorite = isFavorite))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFavorite) Color.Gray else Color.Gray
            )
        ) {
            Text(
                text = if (isFavorite) "В Избранном" else "Добавить в избранное",
                color = if (isFavorite) Color.Red else Color.Black
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        if (cartCount == 0) {
            Button(
                onClick = {
                    cartCount++
                    cartViewModel.addToCart(product)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEB3B))
            ) {
                Text("Добавить в корзину", color = Color.Black)
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = {
                    cartCount--
                    cartViewModel.updateQuantity(product.id, -1)
                }) { Text("-") }
                Text("$cartCount", modifier = Modifier.padding(horizontal = 16.dp))
                Button(onClick = {
                    cartCount++
                    cartViewModel.updateQuantity(product.id, 1)
                }) { Text("+") }
            }
        }
    }
}
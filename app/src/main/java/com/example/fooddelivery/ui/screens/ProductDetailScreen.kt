package com.example.fooddelivery.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
    val images = product.images // Используем список изображений из модели

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Карусель изображений
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(images) { imageUrl ->
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .shadow(8.dp, RoundedCornerShape(12.dp))
                        .background(Color.White)
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                        error = painterResource(androidx.appcompat.R.drawable.abc_ic_menu_paste_mtrl_am_alpha),
                        placeholder = painterResource(androidx.appcompat.R.drawable.abc_ic_menu_paste_mtrl_am_alpha)
                    )
                }
            }
        }

        // Счетчик изображений
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(images.size) { index ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (index == 0) Color.Black else Color.Gray)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Информация о товаре
        Text(product.name, style = MaterialTheme.typography.headlineMedium)
        Text("Цена: ${product.price} ₽", style = MaterialTheme.typography.bodyLarge)
        Text("Категория: ${product.category}", style = MaterialTheme.typography.bodyMedium)
        Text("Подкатегория: ${product.subcategory}", style = MaterialTheme.typography.bodyMedium)
        Text("Описание: ${product.description}", style = MaterialTheme.typography.bodySmall) // Используем описание из модели

        // Характеристики
        Spacer(modifier = Modifier.height(8.dp))
        product.attributes.forEach { (key, value) ->
            Text("$key: $value", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка "Добавить в избранное"
        Button(
            onClick = {
                isFavorite = !isFavorite
                favoritesViewModel.toggleFavorite(product.copy(isFavorite = isFavorite))
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isFavorite) "В Избранном" else "Добавить в избранное",
                color = if (isFavorite) Color.Red else Color.Black,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Кнопка "Добавить в корзину"
        if (cartCount == 0) {
            Button(
                onClick = {
                    cartCount++
                    cartViewModel.addToCart(product)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Добавить в корзину", color = Color.Black)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        cartCount--
                        cartViewModel.updateQuantity(product.id, -1)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) { Text("-") }
                Text(
                    "$cartCount в корзине",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
                Button(
                    onClick = {
                        cartCount++
                        cartViewModel.updateQuantity(product.id, 1)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) { Text("+") }
            }
        }
    }
}
package com.example.fooddelivery.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.fooddelivery.ProfileActivity
import com.example.fooddelivery.viewmodel.CartViewModel

@Composable
fun CartScreen(viewModel: CartViewModel) {
    val cartItemsState = viewModel.cartItems.collectAsState()
    val totalPriceState = viewModel.totalPrice.collectAsState()
    val cartItems = cartItemsState.value
    val totalPrice = totalPriceState.value
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text("Корзина", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        // Таблица товаров
        cartItems.forEachIndexed { index, cartItem ->
            var showDelete by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1-й столбец: Нумерация и название
                Text(
                    "${index + 1}. ${cartItem.product.name}",
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.bodyMedium
                )

                // 2-й столбец: Количество
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { viewModel.updateQuantity(cartItem.product.id, -1) }) { Text("-") }
                    Text("${cartItem.quantity} шт.", modifier = Modifier.padding(horizontal = 8.dp))
                    Button(onClick = { viewModel.updateQuantity(cartItem.product.id, 1) }) { Text("+") }
                }

                // 3-й столбец: Троеточие и удаление
                Box(modifier = Modifier.weight(0.5f)) {
                    if (showDelete) {
                        Button(onClick = { viewModel.removeFromCart(cartItem.product.id) }) {
                            Text("Удалить")
                        }
                    } else {
                        Text(
                            "...",
                            modifier = Modifier.clickable { showDelete = true },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // 4-й столбец: Цена
                Text(
                    "${cartItem.product.price * cartItem.quantity} ₽",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Итог и кнопка оплаты
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Общая цена: $totalPrice ₽", style = MaterialTheme.typography.bodyLarge)
            Button(onClick = {
                context.startActivity(android.content.Intent(context, ProfileActivity::class.java))
            }) {
                Text("Оплатить")
            }
        }
    }
}
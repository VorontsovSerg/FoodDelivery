package com.example.fooddelivery.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fooddelivery.viewmodel.CartViewModel

@Composable
fun CartScreen(viewModel: CartViewModel) {
    val cartItemsState = viewModel.cartItems.collectAsState()
    val totalPriceState = viewModel.totalPrice.collectAsState()
    val cartItems = cartItemsState.value
    val totalPrice = totalPriceState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text("Корзина", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        cartItems.forEachIndexed { index, cartItem ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${index + 1}. ${cartItem.product.name}")
                Row {
                    Button(onClick = { viewModel.updateQuantity(cartItem.product.id, -1) }) { Text("-") }
                    Text("${cartItem.quantity} шт.", modifier = Modifier.padding(horizontal = 8.dp))
                    Button(onClick = { viewModel.updateQuantity(cartItem.product.id, 1) }) { Text("+") }
                }
                Text("${cartItem.product.price * cartItem.quantity} ₽")
                Button(onClick = { viewModel.removeFromCart(cartItem.product.id) }) { Text("Удалить") }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Общая цена: $totalPrice ₽")
            Button(onClick = { /* TODO: Navigate to payment */ }) {
                Text("Оплатить")
            }
        }
    }
}
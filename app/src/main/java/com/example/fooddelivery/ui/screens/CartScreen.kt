package com.example.fooddelivery.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.fooddelivery.PaymentActivity
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

        if (cartItems.isEmpty()) {
            Text(
                "Корзина пуста",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, MaterialTheme.shapes.medium)
                    .background(Color.White)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    itemsIndexed(cartItems) { index, cartItem ->
                        var showDelete by remember { mutableStateOf(false) }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "${index + 1}. ${cartItem.product.name}",
                                modifier = Modifier.weight(2f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(onClick = { viewModel.updateQuantity(cartItem.product.id, -1) }) { Text("-") }
                                Text("${cartItem.quantity} шт.", modifier = Modifier.padding(horizontal = 8.dp))
                                Button(onClick = { viewModel.updateQuantity(cartItem.product.id, 1) }) { Text("+") }
                            }
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
                            Text(
                                "${cartItem.product.price * cartItem.quantity} ₽",
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        if (index < cartItems.size - 1) {
                            DashedDivider(
                                color = Color.Gray,
                                thickness = 1.dp,
                                dashLength = 4.dp,
                                gapLength = 4.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (cartItems.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Общая цена: $totalPrice ₽", style = MaterialTheme.typography.bodyLarge)
                Button(onClick = {
                    val intent = android.content.Intent(context, PaymentActivity::class.java)
                    intent.putExtra("TOTAL_PRICE", totalPrice)
                    context.startActivity(intent)
                }) {
                    Text("Оплатить")
                }
            }
        }
    }
}

@Composable
fun DashedDivider(
    color: Color,
    thickness: Dp,
    dashLength: Dp,
    gapLength: Dp,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current.density
    Canvas(modifier = modifier) {
        val path = Path()
        val width = size.width
        val dash = dashLength.value * density // Преобразуем Dp в Float
        val gap = gapLength.value * density // Преобразуем Dp в Float
        var x = 0f
        while (x < width) {
            path.moveTo(x, size.height / 2)
            x += dash
            path.lineTo(x, size.height / 2)
            x += gap
        }
        drawPath(path, color, style = Stroke(width = thickness.value * density))
    }
}
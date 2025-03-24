package com.example.fooddelivery.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fooddelivery.data.CartItem
import com.example.fooddelivery.viewmodel.CartViewModel
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun CartScreen(viewModel: CartViewModel) {
    val cartItems = viewModel.cartItems.collectAsState().value
    val totalPrice by viewModel.totalPrice.collectAsState()

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
                    .border(1.dp, Color.Black, MaterialTheme.shapes.medium) // Черная обводка
                    .background(Color.White)
                    .padding(8.dp) // Отступы от карточек
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(cartItems) { index, item ->
                        CartItemRow(item, viewModel, cartItems.size > 1, index + 1)
                        if (index < cartItems.size - 1 && cartItems.size > 1) {
                            DashedDivider(
                                color = Color.Black,
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
            Spacer(modifier = Modifier.height(16.dp))
            Text("Итого: $totalPrice ₽", style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Composable
fun CartItemRow(cartItem: CartItem, viewModel: CartViewModel, showQuantityButtons: Boolean, itemNumber: Int) {
    var quantity by remember { mutableStateOf(cartItem.quantity) }
    var expanded by remember { mutableStateOf(false) } // Состояние всплывающего меню

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), // Без фона, только отступы
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Нумерация и название с ценой
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "$itemNumber. ${cartItem.product.name}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black // Черный текст
            )
            Text(
                "${cartItem.product.price} ₽",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black // Черный текст
            )
        }

        // Кнопки +/- для количества (только если > 1 товара)
        if (showQuantityButtons) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        if (quantity > 1) {
                            quantity--
                            viewModel.updateQuantity(cartItem.product.id, -1)
                        } else {
                            viewModel.removeFromCart(cartItem.product.id) // Удаление при 1
                            quantity = 0
                        }
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black // Явно черный текст
                    ),
                    border = BorderStroke(1.dp, Color.Black),
                    modifier = Modifier.size(32.dp)
                ) { Text("-", style = MaterialTheme.typography.bodyMedium) }

                Text(
                    "$quantity",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black // Черный текст
                )

                OutlinedButton(
                    onClick = {
                        quantity++
                        viewModel.updateQuantity(cartItem.product.id, 1)
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black // Явно черный текст
                    ),
                    border = BorderStroke(1.dp, Color.Black),
                    modifier = Modifier.size(32.dp)
                ) { Text("+", style = MaterialTheme.typography.bodyMedium) }
            }
        } else {
            Text(
                "$quantity шт.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Троеточие с всплывающим меню
        Box {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = Color.Black // Черная иконка
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                DropdownMenuItem(
                    text = { Text("Удалить", color = Color.Black) },
                    onClick = {
                        viewModel.removeFromCart(cartItem.product.id)
                        expanded = false
                    }
                )
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
        val dash = dashLength.value * density
        val gap = gapLength.value * density
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
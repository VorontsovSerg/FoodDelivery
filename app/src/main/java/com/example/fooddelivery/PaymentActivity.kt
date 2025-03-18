package com.example.fooddelivery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fooddelivery.ui.theme.FoodDeliveryTheme

class PaymentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val totalPrice = intent.getDoubleExtra("TOTAL_PRICE", 0.0)
        setContent {
            FoodDeliveryTheme {
                PaymentScreen(totalPrice) {
                    finish() // Возврат к MainActivity
                }
            }
        }
    }
}

@Composable
fun PaymentScreen(totalPrice: Double, onBack: () -> Unit) {
    var address by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("Карта") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Верхнее поле с возвратом
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            color = Color.Yellow
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("Оплата", style = MaterialTheme.typography.headlineSmall)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Общая сумма
            Text("Общая сумма: $totalPrice ₽", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Адрес доставки
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Адрес доставки") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Способы оплаты
            Text("Способы оплаты", style = MaterialTheme.typography.bodyLarge)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { paymentMethod = "Карта" },
                    colors = ButtonDefaults.buttonColors(containerColor = if (paymentMethod == "Карта") Color.Yellow else Color.Gray)
                ) { Text("Карта") }
                Button(
                    onClick = { paymentMethod = "СБП" },
                    colors = ButtonDefaults.buttonColors(containerColor = if (paymentMethod == "СБП") Color.Yellow else Color.Gray)
                ) { Text("СБП") }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Кнопка "Оплатить заказ"
            Button(
                onClick = { /* Логика оплаты, пока ничего не делает */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Оплатить заказ")
            }
        }
    }
}
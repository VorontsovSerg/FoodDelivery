package com.example.fooddelivery.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.fooddelivery.data.Product

@Composable
fun ProductCard(
    product: Product,
    onFavoriteClick: (Product) -> Unit,
    onClick: () -> Unit
) {
    val cardColor = when (product.subcategory) {
        "Фрукты" -> Color(0xFF4CAF50)
        "Овощи" -> Color(0xFF8BC34A)
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .width(160.dp)
            .padding(8.dp)
            .background(cardColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                AsyncImage(
                    model = product.imageUrl ?: "error_image_url",
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.Center),
                    contentScale = androidx.compose.ui.layout.ContentScale.Fit
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = (-4).dp, y = (-4).dp)
                        .size(24.dp)
                        .background(
                            if (product.isFavorite) Color.Red else Color.Gray,
                            shape = RoundedCornerShape(50)
                        )
                        .clickable { onFavoriteClick(product) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(product.name, style = MaterialTheme.typography.bodyMedium)

            Row {
                if (product.discount != null) {
                    Text(
                        text = "${product.price * (1 - product.discount)} ₽",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${product.price} ₽",
                        style = MaterialTheme.typography.bodySmall.copy(
                            textDecoration = TextDecoration.LineThrough
                        )
                    )
                } else {
                    Text("${product.price} ₽", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
package com.example.fooddelivery.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                    error = painterResource(androidx.appcompat.R.drawable.abc_ic_menu_paste_mtrl_am_alpha)
                )

                IconButton(
                    onClick = { onFavoriteClick(product) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = if (product.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (product.isFavorite) Color.Red else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(product.name, style = MaterialTheme.typography.bodyMedium)
            Text("${product.price} ₽", style = MaterialTheme.typography.bodySmall)
        }
    }
}
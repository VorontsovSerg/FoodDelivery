package com.example.fooddelivery.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fooddelivery.ui.theme.FoodDeliveryTheme

@Composable
fun SearchBar(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Yellow) // Желтый фон
            .padding(top = 32.dp, bottom = 32.dp) // Отступы сверху и снизу от фона
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { newValue ->
                onQueryChange(newValue)
                onSearch(newValue)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp) // Мелкие отступы справа и слева
                .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                .background(Color.White)
                .align(Alignment.Center), // Центрирование по вертикали
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Black
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear Icon",
                            tint = Color.Black
                        )
                    }
                }
            },
            placeholder = {
                Text(
                    "Поиск",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch(searchQuery) }
            ),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )
    }
}

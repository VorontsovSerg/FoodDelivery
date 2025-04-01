package com.example.fooddelivery.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    horizontalPadding: Dp = 16.dp, // Равные горизонтальные отступы по умолчанию
    verticalPadding: Dp = 24.dp    // Равные вертикальные отступы по умолчанию
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Yellow) // Желтый фоновый цвет за полем поиска
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        shape = MaterialTheme.shapes.medium // Закругленные края для всей области
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White), // Белый фон для поля ввода
            placeholder = { Text("Поиск продуктов...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Gray
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch(searchQuery) }),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, // Черная обводка при фокусе
                unfocusedBorderColor = Color.Black, // Черная обводка без фокуса
                cursorColor = Color.Gray
            ),
            shape = MaterialTheme.shapes.medium // Равные закругленные края для поля ввода
        )
    }
}
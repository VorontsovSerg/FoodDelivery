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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit
) {
    val isFocused = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Yellow)
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { newValue ->
                onQueryChange(newValue) // Только обновляем текст, поиск не запускается
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                .background(Color.White)
                .align(Alignment.Center)
                .onFocusChanged { focusState ->
                    isFocused.value = focusState.isFocused
                    onFocusChange(focusState.isFocused) // Уведомляем о фокусе
                },
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
                onSearch = {
                    if (searchQuery.isNotEmpty()) {
                        onSearch(searchQuery) // Поиск только при нажатии Enter и непустой строке
                    }
                }
            ),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )
    }
}
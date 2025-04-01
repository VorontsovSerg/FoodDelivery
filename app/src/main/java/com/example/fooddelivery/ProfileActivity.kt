package com.example.fooddelivery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileScreen()
        }
    }

    @Composable
    fun ProfileScreen() {
        var avatarUri by remember { mutableStateOf<Uri?>(null) } // Хранит URI выбранного аватара
        var isEditing by remember { mutableStateOf(false) } // Режим редактирования

        // Логика выбора изображения из галереи
        val pickImageLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            avatarUri = uri
            isEditing = false // Выходим из режима редактирования после выбора
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Аватар с круглой маской
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape) // Круглая маска
                    .background(Color.LightGray) // Фон по умолчанию
                    .clickable(enabled = isEditing) {
                        pickImageLauncher.launch("image/*") // Открываем галерею в режиме редактирования
                    },
                contentAlignment = Alignment.Center
            ) {
                if (avatarUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(avatarUri),
                        contentDescription = "Profile Avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop // Масштабирование по центру с обрезкой
                    )
                } else {
                    Text(
                        text = "Нет аватара",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка редактирования
            IconButton(
                onClick = { isEditing = !isEditing },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile",
                    tint = if (isEditing) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Остальная информация профиля (пример из репозитория)
            Text(
                text = "Имя пользователя",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "email@example.com",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}
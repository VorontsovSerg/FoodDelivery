package com.example.fooddelivery.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.fooddelivery.data.Persistence
import com.example.fooddelivery.data.ProfileData
import java.io.File
import java.io.FileOutputStream

@Composable
fun EditProfileScreen(
    navController: NavController,
    initialProfile: ProfileData,
    onProfileUpdated: (ProfileData) -> Unit
) {
    val context = LocalContext.current
    var username by remember { mutableStateOf(initialProfile.username) }
    var email by remember { mutableStateOf(initialProfile.email) }
    var phone by remember { mutableStateOf(TextFieldValue(initialProfile.phone ?: "+7", TextRange(2))) }
    var avatarUri by remember { mutableStateOf(initialProfile.avatarUri) }

    // Логика выбора изображения из галереи
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Копируем изображение в локальное хранилище
            val inputStream = context.contentResolver.openInputStream(it)
            val file = File(context.cacheDir, "avatar_${System.currentTimeMillis()}.jpg")
            inputStream?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            avatarUri = file.absolutePath // Сохраняем путь к файлу
        }
    }

    // Форматирование номера телефона
    fun formatPhoneNumber(input: String): Pair<String, Int> {
        val digits = input.filter { it.isDigit() }
        val formatted = when {
            digits.isEmpty() -> "+7"
            digits.length <= 1 -> "+7${digits.take(1)}"
            digits.length <= 4 -> "+7(${digits.drop(1).take(3)}"
            digits.length <= 7 -> "+7(${digits.drop(1).take(3)})${digits.drop(4).take(3)}"
            digits.length <= 9 -> "+7(${digits.drop(1).take(3)})${digits.drop(4).take(3)}-${digits.drop(7).take(2)}"
            else -> "+7(${digits.drop(1).take(3)})${digits.drop(4).take(3)}-${digits.drop(7).take(2)}-${digits.drop(9).take(2)}"
        }
        val cursorPos = when {
            digits.length <= 1 -> formatted.length
            digits.length <= 4 -> formatted.length
            digits.length <= 7 -> formatted.length
            digits.length <= 9 -> formatted.length
            else -> formatted.length
        }
        return Pair(formatted, cursorPos)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Редактировать профиль",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Аватар с круглой маской
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable { pickImageLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (avatarUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(avatarUri),
                    contentDescription = "Profile Avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
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

        // Поле для имени пользователя
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Имя пользователя", color = Color.Black) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                if (username.isNotEmpty()) {
                    IconButton(onClick = { username = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Очистить")
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Поле для email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = Color.Black) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                if (email.isNotEmpty()) {
                    IconButton(onClick = { email = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Очистить")
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Поле для телефона
        OutlinedTextField(
            value = phone,
            onValueChange = { newValue ->
                val digits = newValue.text.filter { it.isDigit() }
                if (digits.length <= 11) {
                    val (formatted, cursorPos) = formatPhoneNumber(newValue.text)
                    phone = TextFieldValue(formatted, TextRange(cursorPos))
                }
            },
            label = { Text("Номер телефона", color = Color.Black) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            trailingIcon = {
                if (phone.text.isNotEmpty() && phone.text != "+7") {
                    IconButton(onClick = { phone = TextFieldValue("+7", TextRange(2)) }) {
                        Icon(Icons.Default.Clear, contentDescription = "Очистить")
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка сохранения
        OutlinedButton(
            onClick = {
                val updatedProfile = ProfileData(
                    avatarUri = avatarUri,
                    username = username,
                    email = email,
                    phone = phone.text.takeIf { it != "+7" }
                )
                Persistence.saveProfile(context, updatedProfile)
                onProfileUpdated(updatedProfile)
                navController.popBackStack()
            },
            enabled = username.isNotEmpty(),
            modifier = Modifier
                .padding(horizontal = 16.dp),
            shape = MaterialTheme.shapes.small,
            border = BorderStroke(1.dp, Color.Black),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Black,
                containerColor = Color.Transparent
            )
        ) {
            Text("Сохранить")
        }
    }
}
package com.example.fooddelivery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.fooddelivery.data.Persistence
import com.example.fooddelivery.data.ProfileData
import com.example.fooddelivery.ui.screens.EditProfileScreen

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileScreen(textSize = 18.sp) // Теперь тип TextUnit
        }
    }

    @Composable
    fun ProfileScreen(textSize: TextUnit) { // Изменен тип с Float на TextUnit
        val navController = rememberNavController()
        var profile by remember { mutableStateOf(Persistence.loadProfile(this) ?: ProfileData()) }

        NavHost(navController = navController, startDestination = "profile") {
            composable("profile") {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally // Аватар по центру
                ) {
                    // Кнопка редактирования профиля в правом верхнем углу
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = { navController.navigate("editProfile") }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = Color.Gray
                            )
                        }
                    }

                    // Аватар с круглой маской
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        if (profile.avatarUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(profile.avatarUri),
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

                    Spacer(modifier = Modifier.height(24.dp))

                    // Информация профиля в 3 строки с выравниванием текста по левому краю
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Start) // Выравнивание текста по левому краю
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                append("Имя: ")
                                pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                                append(profile.username)
                                pop()
                            },
                            style = TextStyle(fontSize = textSize, color = Color.Black) // Используем TextUnit
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = buildAnnotatedString {
                                append("Email: ")
                                pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                                append(profile.email)
                                pop()
                            },
                            style = TextStyle(fontSize = textSize, color = Color.Black)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = buildAnnotatedString {
                                append("Телефон: ")
                                pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                                append(profile.phone ?: "Не указан")
                                pop()
                            },
                            style = TextStyle(fontSize = textSize, color = Color.Black)
                        )
                    }
                }
            }
            composable("editProfile") {
                EditProfileScreen(
                    navController = navController,
                    initialProfile = profile,
                    onProfileUpdated = { updatedProfile ->
                        profile = updatedProfile // Автоматическое обновление
                    }
                )
            }
        }
    }
}
package com.example.fooddelivery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.fooddelivery.data.Persistence
import com.example.fooddelivery.ui.theme.FoodDeliveryTheme

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodDeliveryTheme {
                ProfileScreen(onBack = { finish() })
            }
        }
    }
}

data class ProfileData(
    var avatarUrl: String? = null,
    var fullName: String? = null,
    var city: String? = null,
    var phone: String? = null
)

@Composable
fun ProfileScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var profileData by remember { mutableStateOf(Persistence.loadProfile(context) ?: ProfileData()) }
    var isEditing by remember { mutableStateOf(false) }
    var tempAvatarUrl by remember { mutableStateOf(profileData.avatarUrl ?: "") }
    var tempFullName by remember { mutableStateOf(profileData.fullName ?: "") }
    var tempCity by remember { mutableStateOf(profileData.city ?: "") }
    var tempPhone by remember { mutableStateOf(profileData.phone ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("Профиль", style = MaterialTheme.typography.headlineSmall)
            IconButton(onClick = { isEditing = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isEditing) {
            OutlinedTextField(
                value = tempAvatarUrl,
                onValueChange = { tempAvatarUrl = it },
                label = { Text("URL аватара") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = tempFullName,
                onValueChange = { tempFullName = it },
                label = { Text("ФИО") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = tempCity,
                onValueChange = { tempCity = it },
                label = { Text("Город") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = tempPhone,
                onValueChange = { tempPhone = it },
                label = { Text("Номер телефона") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    profileData = ProfileData(tempAvatarUrl, tempFullName, tempCity, tempPhone)
                    Persistence.saveProfile(context, profileData)
                    isEditing = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить")
            }
        } else {
            Text("Аватар: ${profileData.avatarUrl ?: "Пусто"}", style = MaterialTheme.typography.bodyLarge)
            Text("ФИО: ${profileData.fullName ?: "Пусто"}", style = MaterialTheme.typography.bodyLarge)
            Text("Город: ${profileData.city ?: "Пусто"}", style = MaterialTheme.typography.bodyLarge)
            Text("Телефон: ${profileData.phone ?: "Пусто"}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
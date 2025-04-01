package com.example.fooddelivery.ui.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fooddelivery.ProfileActivity

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    val context = LocalContext.current

    NavigationBar(
        modifier = Modifier
            .background(Color.LightGray)
            .height(64.dp), // Стандартная высота нижней панели
        containerColor = Color.LightGray
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween, // Равномерное распределение
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = { navController.navigate("home") },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = if (currentRoute == "home") MaterialTheme.colorScheme.primary else Color.DarkGray
                )
            }

            IconButton(
                onClick = { navController.navigate("catalog") },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "Catalog",
                    tint = if (currentRoute == "catalog") MaterialTheme.colorScheme.primary else Color.DarkGray
                )
            }

            IconButton(
                onClick = { navController.navigate("favorites") },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorites",
                    tint = if (currentRoute == "favorites") MaterialTheme.colorScheme.primary else Color.DarkGray
                )
            }

            IconButton(
                onClick = { navController.navigate("cart") },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Cart",
                    tint = if (currentRoute == "cart") MaterialTheme.colorScheme.primary else Color.DarkGray
                )
            }

            IconButton(
                onClick = {
                    val intent = Intent(context, ProfileActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.DarkGray
                )
            }
        }
    }
}
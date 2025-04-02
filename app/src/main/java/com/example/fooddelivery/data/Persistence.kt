package com.example.fooddelivery.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Persistence {
    private const val PREFS_NAME = "FoodDeliveryPrefs"
    private const val KEY_FAVORITES = "favorites"
    private const val KEY_CART = "cart"
    private const val KEY_PROFILE = "profile"

    private fun getPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Избранное
    fun saveFavorites(context: Context, favoriteIds: Set<Int>) {
        getPrefs(context).edit().putStringSet(KEY_FAVORITES, favoriteIds.map { it.toString() }.toSet()).apply()
    }

    fun loadFavorites(context: Context): Set<Int> {
        val favorites = getPrefs(context).getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
        return favorites.map { it.toInt() }.toSet()
    }

    // Корзина
    fun saveCart(context: Context, cartItems: List<CartItem>) {
        val gson = Gson()
        val json = gson.toJson(cartItems)
        getPrefs(context).edit().putString(KEY_CART, json).apply()
    }

    fun loadCart(context: Context): List<CartItem> {
        val gson = Gson()
        val json = getPrefs(context).getString(KEY_CART, null)
        return if (json != null) {
            val type = object : TypeToken<List<CartItem>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    // Профиль
    fun saveProfile(context: Context, profile: ProfileData) {
        val gson = Gson()
        val json = gson.toJson(profile)
        getPrefs(context).edit().putString(KEY_PROFILE, json).apply()
    }

    fun loadProfile(context: Context): ProfileData? {
        val gson = Gson()
        val json = getPrefs(context).getString(KEY_PROFILE, null)
        return if (json != null) {
            gson.fromJson(json, ProfileData::class.java)
        } else {
            null
        }
    }
}
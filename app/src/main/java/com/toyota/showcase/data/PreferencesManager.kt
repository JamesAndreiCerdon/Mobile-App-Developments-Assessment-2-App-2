package com.toyota.showcase.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {

    companion object {
        private val NIGHT_MODE = booleanPreferencesKey("night_mode")
        private fun favoriteKey(carName: String) = booleanPreferencesKey("favorite_$carName")
    }

    // Theme preferences
    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[NIGHT_MODE] ?: false
        }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NIGHT_MODE] = enabled
        }
    }

    // Favorites preferences
    fun isFavorite(carName: String): Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[favoriteKey(carName)] ?: false
        }

    suspend fun setFavorite(carName: String, isFavorite: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[favoriteKey(carName)] = isFavorite
        }
    }

    fun getAllFavorites(): Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences.asMap()
                .filterKeys { it.name.startsWith("favorite_") }
                .filterValues { it as Boolean }
                .mapKeys { it.key.name.removePrefix("favorite_") }
                .keys
        }
} 
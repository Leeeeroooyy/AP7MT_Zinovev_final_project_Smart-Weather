package com.vadim_zinovev.smartweather.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.favoritesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "favorite_cities"
)

class FavoritesStorage(
    private val context: Context
) {
    private val FAVORITE_CITIES_KEY = stringSetPreferencesKey("favorite_cities")

    val favoriteCities: Flow<List<String>> =
        context.favoritesDataStore.data.map { prefs ->
            prefs[FAVORITE_CITIES_KEY]?.toList() ?: emptyList()
        }

    suspend fun toggleCity(cityName: String) {
        context.favoritesDataStore.edit { prefs ->
            val current = prefs[FAVORITE_CITIES_KEY] ?: emptySet()
            prefs[FAVORITE_CITIES_KEY] =
                if (current.contains(cityName)) {
                    current - cityName
                } else {
                    current + cityName
                }
        }
    }

    suspend fun removeCity(cityName: String) {
        context.favoritesDataStore.edit { prefs ->
            val current = prefs[FAVORITE_CITIES_KEY] ?: emptySet()
            prefs[FAVORITE_CITIES_KEY] = current - cityName
        }
    }

    suspend fun isFavorite(cityName: String): Boolean {
        val current = context.favoritesDataStore.data.first()[FAVORITE_CITIES_KEY] ?: emptySet()
        return current.contains(cityName)
    }
}

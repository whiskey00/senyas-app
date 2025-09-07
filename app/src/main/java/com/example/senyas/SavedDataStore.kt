package com.example.senyas.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.senyas.model.SavedItem
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.savedDataStore by preferencesDataStore("saved_translations")

object SavedDataStore {
    private val SAVED_KEY = stringPreferencesKey("saved_list")

    suspend fun saveItem(context: Context, item: SavedItem) {
        val prefs = context.savedDataStore.data.first()
        val currentList = prefs[SAVED_KEY]?.let {
            Json.decodeFromString<List<SavedItem>>(it)
        } ?: emptyList()

        // Check if item already exists
        if (currentList.any { it.text == item.text }) {
            return // Already saved
        }

        val updatedList = (listOf(item) + currentList).take(100) // Limit to 100
        context.savedDataStore.edit {
            it[SAVED_KEY] = Json.encodeToString(updatedList)
        }
    }

    suspend fun loadSaved(context: Context): List<SavedItem> {
        val prefs = context.savedDataStore.data.first()
        return prefs[SAVED_KEY]?.let {
            Json.decodeFromString(it)
        } ?: emptyList()
    }

    suspend fun deleteSavedByText(context: Context, text: String) {
        val prefs = context.savedDataStore.data.first()
        val currentList = prefs[SAVED_KEY]?.let {
            Json.decodeFromString<List<SavedItem>>(it)
        } ?: return

        val updatedList = currentList.filterNot { it.text == text }
        context.savedDataStore.edit {
            it[SAVED_KEY] = Json.encodeToString(updatedList)
        }
    }

    suspend fun isItemSaved(context: Context, text: String): Boolean {
        val savedItems = loadSaved(context)
        return savedItems.any { it.text == text }
    }
}

package com.example.senyas.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.senyas.model.TranslationHistoryItem
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore by preferencesDataStore("translation_history")

object HistoryDataStore {
    private val HISTORY_KEY = stringPreferencesKey("history_list")

    suspend fun saveTranslation(context: Context, item: TranslationHistoryItem) {
        val prefs = context.dataStore.data.first()
        val currentList = prefs[HISTORY_KEY]?.let {
            Json.decodeFromString<List<TranslationHistoryItem>>(it)
        } ?: emptyList()

        val updatedList = (listOf(item) + currentList).take(50) // Limit to 50
        context.dataStore.edit {
            it[HISTORY_KEY] = Json.encodeToString(updatedList)
        }
    }

    suspend fun loadHistory(context: Context): List<TranslationHistoryItem> {
        val prefs = context.dataStore.data.first()
        return prefs[HISTORY_KEY]?.let {
            Json.decodeFromString(it)
        } ?: emptyList()
    }

    suspend fun deleteHistoryByText(context: Context, text: String) {
        val prefs = context.dataStore.data.first()
        val currentList = prefs[HISTORY_KEY]?.let {
            Json.decodeFromString<List<TranslationHistoryItem>>(it)
        } ?: return

        val updatedList = currentList.filterNot { it.text == text }
        context.dataStore.edit {
            it[HISTORY_KEY] = Json.encodeToString(updatedList)
        }
    }
}

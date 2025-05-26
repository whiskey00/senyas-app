package com.example.senyas.model

import kotlinx.serialization.Serializable

@Serializable
data class TranslationHistoryItem(
    val text: String,
    val timestamp: Long
)

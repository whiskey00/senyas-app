package com.example.senyas.model

import kotlinx.serialization.Serializable

@Serializable
data class SavedItem(
    val text: String,
    val timestamp: Long
)

package com.example.senyas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.senyas.datastore.HistoryDataStore
import com.example.senyas.model.TranslationHistoryItem
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.net.Uri
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch


@Composable
fun HistoryScreen(onBack: () -> Unit = {}, navController: NavHostController) {
    val context = LocalContext.current
    var history by remember { mutableStateOf<List<TranslationHistoryItem>>(emptyList()) }
    val scope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        history = HistoryDataStore.loadHistory(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFF0F172A))
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Translation History", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (history.isEmpty()) {
            Text("No translation history yet.", color = Color.Gray)
        } else {
            history.forEach { item ->
                val timeString = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault()).format(Date(item.timestamp))
                HistoryItem(
                    text = item.text,
                    time = timeString,
                    onPlayClick = {
                        navController.navigate("home?playText=${Uri.encode(item.text)}") {
                            popUpTo("history") { inclusive = true }
                        }
                    },
                    onDeleteClick = {
                        scope.launch {
                            HistoryDataStore.deleteHistoryByText(context, item.text)
                            history = HistoryDataStore.loadHistory(context) // refresh list
                        }
                    }
                )
            }
        }
    }
}
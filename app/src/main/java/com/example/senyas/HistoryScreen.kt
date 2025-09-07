package com.example.senyas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.senyas.datastore.HistoryDataStore
import com.example.senyas.datastore.SavedDataStore
import com.example.senyas.model.TranslationHistoryItem
import com.example.senyas.model.SavedItem
import com.example.senyas.ui.theme.SenyasColors
import java.text.SimpleDateFormat
import java.util.*
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SenyasColors.Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = SenyasColors.OnSurface
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Translation History",
                        style = MaterialTheme.typography.titleLarge,
                        color = SenyasColors.OnSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${history.size} translations",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SenyasColors.OnSurfaceVariant
                    )
                }
                IconButton(
                    onClick = {
                        scope.launch {
                            // Clear all history
                            history.forEach { item ->
                                HistoryDataStore.deleteHistoryByText(context, item.text)
                            }
                            history = emptyList()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "Clear All",
                        tint = SenyasColors.Error
                    )
                }
            }

            if (history.isEmpty()) {
                // Empty State
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = SenyasColors.OnSurfaceLight,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No Translation History",
                        color = SenyasColors.OnSurface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Your translated FSL phrases will appear here for easy access and replay.",
                        color = SenyasColors.OnSurfaceVariant,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            navController.navigate("home") {
                                popUpTo("history") { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SenyasColors.Primary,
                            contentColor = SenyasColors.OnPrimary
                        )
                    ) {
                        Text("Start Translating")
                    }
                }
            } else {
                // History List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(history) { item ->
                        ModernHistoryItem(
                            text = item.text,
                            time = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault()).format(Date(item.timestamp)),
                            onPlayClick = {
                                navController.navigate("home?playText=${Uri.encode(item.text)}") {
                                    popUpTo("history") { inclusive = true }
                                }
                            },
                            onSaveClick = {
                                scope.launch {
                                    SavedDataStore.saveItem(
                                        context,
                                        SavedItem(
                                            text = item.text,
                                            timestamp = System.currentTimeMillis()
                                        )
                                    )
                                }
                            },
                            onDeleteClick = {
                                scope.launch {
                                    HistoryDataStore.deleteHistoryByText(context, item.text)
                                    history = HistoryDataStore.loadHistory(context)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ModernHistoryItem(
    text: String,
    time: String,
    onPlayClick: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SenyasColors.Surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                                        .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Text Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = text.capitalize(),
                    style = MaterialTheme.typography.titleMedium,
                    color = SenyasColors.OnSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = time,
                    style = MaterialTheme.typography.bodySmall,
                    color = SenyasColors.OnSurfaceVariant
                )
            }

            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Play Button
                IconButton(
                    onClick = onPlayClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = SenyasColors.Primary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Save Button
                IconButton(
                    onClick = onSaveClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkAdd,
                        contentDescription = "Save",
                        tint = SenyasColors.Primary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Delete Button
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = SenyasColors.Error,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
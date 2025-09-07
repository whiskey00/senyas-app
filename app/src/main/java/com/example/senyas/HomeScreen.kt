package com.example.senyas

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.media3.ui.AspectRatioFrameLayout
import com.example.senyas.datastore.HistoryDataStore
import com.example.senyas.model.TranslationHistoryItem
import com.example.senyas.ui.theme.SenyasColors
import com.example.senyas.ui.components.ModernNotificationHost
import com.example.senyas.ui.components.rememberNotificationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onSavedClick: () -> Unit = {},
    onLearnFSLClick: () -> Unit = {},
    playFromHistory: String? = null,
) {
    var inputText by remember { mutableStateOf("") }
    val notificationState = rememberNotificationState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var videoToPlay by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            playWhenReady = true
        }
    }

    LaunchedEffect(videoToPlay) {
        videoToPlay?.let { file ->
            try {
                val resourceName = file.replace(".mp4", "").lowercase()
                val resourceId = context.resources.getIdentifier(resourceName, "raw", context.packageName)
                
                if (resourceId != 0) {
                    val uri = Uri.parse("android.resource://${context.packageName}/$resourceId")
                    player.setMediaItem(MediaItem.fromUri(uri))
                    player.prepare()
                    player.playWhenReady = true
                } else {
                    // Show error message if resource not found
                    scope.launch {
                        notificationState.showError("Video file not found: $resourceName")
                    }
                }
            } catch (e: Exception) {
                scope.launch {
                    notificationState.showError("Error loading video: ${e.message}")
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        if (!playFromHistory.isNullOrBlank()) {
            val matched = loadGlossFileAndMatch(playFromHistory.lowercase(), context)
            if (matched != null) {
                videoToPlay = matched
            } else {
                scope.launch {
                    notificationState.showError("History: No matching FSL found.")
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { player.release() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SenyasColors.Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Modern Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_white),
                            contentDescription = "Senyas Logo",
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                "SENYAS",
                                style = MaterialTheme.typography.titleLarge,
                                color = SenyasColors.OnSurface,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Filipino Sign Language Translator",
                                style = MaterialTheme.typography.bodySmall,
                                color = SenyasColors.OnSurfaceVariant
                            )
                        }
                    }
                    
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = SenyasColors.OnSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Video Display Area
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SenyasColors.Surface
                    )
                ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (videoToPlay != null) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            AndroidView(
                                factory = {
                                    PlayerView(it).apply {
                                        this.player = player
                                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                                        useController = false
                                        // Shift video up to focus on upper body
                                        translationY = -40f
                                        scaleX = 1.2f
                                        scaleY = 1.2f
                                    }
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayCircleOutline,
                                contentDescription = null,
                                tint = SenyasColors.OnSurfaceLight,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Your translated FSL will appear here",
                                style = MaterialTheme.typography.bodyMedium,
                                color = SenyasColors.OnSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "Enter text below to get started",
                                style = MaterialTheme.typography.bodySmall,
                                color = SenyasColors.OnSurfaceLight,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

                // Input Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SenyasColors.Surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                    Text(
                        "Translate to FSL",
                        style = MaterialTheme.typography.titleMedium,
                        color = SenyasColors.OnSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { 
                                Text(
                                    "Enter Filipino text to translate...",
                                    color = SenyasColors.OnSurfaceLight
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SenyasColors.Primary,
                                unfocusedBorderColor = SenyasColors.Border,
                                focusedTextColor = SenyasColors.OnSurface,
                                unfocusedTextColor = SenyasColors.OnSurface,
                                focusedContainerColor = SenyasColors.SurfaceVariant,
                                unfocusedContainerColor = SenyasColors.SurfaceVariant
                            ),
                            maxLines = 5
                        )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Button(
                        onClick = {
                            scope.launch {
                                isLoading = true
                                val cleanedText = inputText.trim().lowercase()
                                val matched = loadGlossFileAndMatch(cleanedText, context)

                                if (matched != null) {
                                    videoToPlay = matched
                                    // Save to history
                                    HistoryDataStore.saveTranslation(
                                        context,
                                        TranslationHistoryItem(
                                            text = cleanedText,
                                            timestamp = System.currentTimeMillis()
                                        )
                                    )
                                    notificationState.showSuccess("Translation successful!")
                                } else {
                                    notificationState.showError("No matching FSL translation found.")
                                }
                                isLoading = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SenyasColors.Primary,
                            contentColor = SenyasColors.OnPrimary
                        ),
                        enabled = inputText.isNotBlank() && !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = SenyasColors.OnPrimary,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Translate,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Translate to FSL",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            }
            
            // Navigation Cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onHistoryClick() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SenyasColors.Surface
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "History",
                            tint = SenyasColors.Primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "History",
                            color = SenyasColors.OnSurfaceVariant,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSavedClick() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SenyasColors.Surface
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "Saved",
                            tint = SenyasColors.Primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Saved",
                            color = SenyasColors.OnSurfaceVariant,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onLearnFSLClick() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SenyasColors.Surface
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Book,
                            contentDescription = "Learn",
                            tint = SenyasColors.Primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Learn",
                            color = SenyasColors.OnSurfaceVariant,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // Modern Notification
        ModernNotificationHost(
            notificationState = notificationState,
            modifier = Modifier.fillMaxSize()
        )
    }
}

suspend fun loadGlossFileAndMatch(text: String, context: Context): String? = withContext(Dispatchers.IO) {
    return@withContext try {
        val jsonStr = context.assets.open("gloss_model.json").bufferedReader().use { it.readText() }
        val jsonArray = JSONObject(jsonStr).getJSONArray("translations")
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            if (obj.getString("input").lowercase() == text) {
                return@withContext "${obj.getString("gloss")}"
            }
        }
        null
    } catch (e: Exception) {
        null
    }
}
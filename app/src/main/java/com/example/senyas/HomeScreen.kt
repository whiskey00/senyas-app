package com.example.senyas

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import androidx.media3.ui.AspectRatioFrameLayout
import com.example.senyas.datastore.HistoryDataStore
import com.example.senyas.model.TranslationHistoryItem
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)

@Composable
fun HomeScreen(
    onLogout: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onLearnFSLClick: () -> Unit = {},
    playFromHistory: String? = null,
) {
    var inputText by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var videoToPlay by remember { mutableStateOf<String?>(null) }

    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            playWhenReady = true
        }
    }

    LaunchedEffect(videoToPlay) {
        videoToPlay?.let { file ->
            val uri = Uri.parse("android.resource://${context.packageName}/raw/${file}")
            player.setMediaItem(MediaItem.fromUri(uri))
            player.prepare()
            player.playWhenReady = true 
        }
    }

    LaunchedEffect(Unit) {
        if (!playFromHistory.isNullOrBlank()) {
            val matched = loadGlossFileAndMatch(playFromHistory.lowercase(), context)
            if (matched != null) {
                videoToPlay = matched
            } else {
                snackbarHostState.showSnackbar("History: No matching FSL found.")
            }
        }
    }



    DisposableEffect(Unit) {
        onDispose { player.release() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_white),
                        contentDescription = "Senyas Logo",
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        "SENYAS",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }

                Row {
                    IconButton(onClick = { /* TODO: Dark mode toggle */ }) {
                        Icon(Icons.Default.WbSunny, contentDescription = null, tint = Color.White)
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = null, tint = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Avatar / Video box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1E293B))
                    .clipToBounds(),
                contentAlignment = Alignment.Center
            ) {
                if (videoToPlay != null) {
                    AndroidView(
                        factory = {
                            PlayerView(it).apply {
                                this.player = player
                                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                                useController = false
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        "Your translated FSL will appear here.",
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Enter text to translate to FSL...", color = Color.LightGray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color(0xFF1E293B)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch {
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
                        } else {
                            snackbarHostState.showSnackbar("No matching FSL translation found.")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B82F6),
                    contentColor = Color.White
                )
            ) {
                Text("\uD83C\uDD24 Translate to FSL")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { /* TODO: Feedback */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF384454),
                    contentColor = Color.White
                )
            ) {
                Text("\uD83D\uDCAC Give Feedback")
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom Nav Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf(
                    "History" to Icons.Default.History,
                    "Saved" to Icons.Default.Bookmark,
                    "Learn FSL" to Icons.Default.Book
                ).forEach { (label, icon) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .width(120.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E293B))
                            .clickable {
                                when (label) {
                                    "History" -> onHistoryClick()
                                    "Learn FSL" -> onLearnFSLClick()
                                }
                            }
                            .padding(horizontal = 25.dp, vertical = 12.dp)
                    ) {
                        Icon(icon, contentDescription = null, tint = Color(0xFF60A5FA))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(label, color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }

        // Floating snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
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
                return@withContext "fsl${obj.getString("gloss")}"
            }
        }
        null
    } catch (e: Exception) {
        null
    }
}
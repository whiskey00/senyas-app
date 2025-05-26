package com.example.senyas

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.core.net.toUri

data class FSLClip(val title: String, val rawFileName: String, val description: String)

val pagbatiClips = listOf(
    FSLClip("Kamusta", "kamusta", "Pagbati kapag nakikita ang isang tao"),
    FSLClip("Kamusta Ka", "kamusta_ka", "Pagbati na may kasamang pagtanong sa kalagayan"),
    FSLClip("Magandang Umaga", "magandang_umaga", "Pagbati sa umaga"),
    FSLClip("Magandang Hapon", "magandang_hapon", "Pagbati sa hapon"),
    FSLClip("Magandang Gabi", "magandang_gabi", "Pagbati sa gabi"),
    FSLClip("Ikinagagalak Kitang Makilala", "ikinagagalak_kitang_makilala", "Pormal na pagbati"),
    FSLClip("Salamat", "salamat", "Pasasalamat o appreciation"),
    FSLClip("Walang Anuman", "walang_anuman", "Pagtugon sa pasasalamat")
)

val numeroClips = listOf(
    FSLClip("Isa", "isa", "Bilang na isa"),
    FSLClip("Dalawa", "dalawa", "Bilang na dalawa"),
    FSLClip("Tatlo", "tatlo", "Bilang na tatlo"),
    FSLClip("Apat", "apat", "Bilang na apat"),
    FSLClip("Lima", "lima", "Bilang na lima"),
    FSLClip("Anim", "anim", "Bilang na anim"),
    FSLClip("Pito", "pito", "Bilang na pito"),
    FSLClip("Walo", "walo", "Bilang na walo"),
    FSLClip("Siyam", "siyam", "Bilang na siyam"),
    FSLClip("Sampo", "sampo", "Bilang na sampo")
)

enum class LearnFSLState { CATEGORY, LIST, DETAIL }

@Composable
fun LearnFSLScreen(onBack: () -> Unit = {}) {
    var state by remember { mutableStateOf(LearnFSLState.CATEGORY) }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedClip by remember { mutableStateOf<FSLClip?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                when (state) {
                    LearnFSLState.DETAIL -> state = LearnFSLState.LIST
                    LearnFSLState.LIST -> state = LearnFSLState.CATEGORY
                    LearnFSLState.CATEGORY -> onBack()
                }
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                "Learn FSL",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            LearnFSLState.CATEGORY -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("Pagbati", "Numero").forEach { category ->
                        Button(
                            onClick = {
                                selectedCategory = category
                                state = LearnFSLState.LIST
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B))
                        ) {
                            Text(category, color = Color.White)
                        }
                    }
                }
            }

            LearnFSLState.LIST -> {
                val clips = if (selectedCategory == "Pagbati") pagbatiClips else numeroClips
                Text(
                    selectedCategory,
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(clips) { clip ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .clickable {
                                    selectedClip = clip
                                    state = LearnFSLState.DETAIL
                                },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(clip.title, color = Color.White)
                            }
                        }
                    }
                }
            }

            LearnFSLState.DETAIL -> {
                selectedClip?.let { clip ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = selectedCategory,
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                        ) {
                            VideoPlayer(clip = clip)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = clip.title,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                        ) {
                            Text(
                                text = clip.description,
                                color = Color(0xFF9CA3AF),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun VideoPlayer(clip: FSLClip) {
    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            val uri = "android.resource://${context.packageName}/raw/${clip.rawFileName}".toUri()
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = true
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
        }
    }

    DisposableEffect(Unit) {
        onDispose { player.release() }
    }

    AndroidView(factory = {
        PlayerView(it).apply {
            this.player = player
            useController = true
        }
    }, modifier = Modifier
        .fillMaxWidth()
        .height(200.dp))
}

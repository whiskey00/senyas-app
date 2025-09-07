package com.example.senyas

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.core.net.toUri
import com.example.senyas.ui.theme.SenyasColors

data class FSLClip(val title: String, val rawFileName: String, val description: String)

val pagbatiClips = listOf(
    FSLClip("Magandang Umaga", "magandang_umaga", "Good Morning"),
    FSLClip("Magandang Hapon", "magandang_hapon", "Good Afternoon"),
    FSLClip("Magandang Gabi", "magandang_gabi", "Good Evening"),
    FSLClip("Kumusta", "kumusta", "Hello"),
    FSLClip("Kumusta Ka", "kumusta_ka", "How Are You"),
    FSLClip("Ayos Lang Ako", "ayos_lang_ako", "I'm Fine"),
    FSLClip("Ikinagagalak Kitang Makilala", "ikinagagalak_kitang_makilala", "Nice To Meet You"),
    FSLClip("Salamat", "salamat", "Thank You"),
    FSLClip("Walang Anuman", "walang_anuman", "You're Welcome"),
    FSLClip("Magkita Tayo Bukas", "magkita_tayo_bukas", "See You Tomorrow"),
    FSLClip("Naiintindihan", "naiintindihan", "Understand"),
    FSLClip("Hindi Naintindihan", "hindi_naintindihan", "Don't Understand"),
    FSLClip("Alam", "alam", "Know"),
    FSLClip("Hindi Alam", "hindi_alam", "Don't Know"),
    FSLClip("Hindi", "hindi", "No"),
    FSLClip("Oo", "oo", "Yes"),
    FSLClip("Mali", "mali", "Wrong"),
    FSLClip("Tama", "tama", "Correct"),
    FSLClip("Mabagal", "mabagal", "Slow"),
    FSLClip("Mabilis", "mabilis", "Fast")
)

val numeroClips = listOf(
    FSLClip("Isa", "isa", "One"),
    FSLClip("Dalawa", "dalawa", "Two"),
    FSLClip("Tatlo", "tatlo", "Three"),
    FSLClip("Apat", "apat", "Four"),
    FSLClip("Lima", "lima", "Five"),
    FSLClip("Anim", "anim", "Six"),
    FSLClip("Pito", "pito", "Seven"),
    FSLClip("Walo", "walo", "Eight"),
    FSLClip("Siyam", "siyam", "Nine"),
    FSLClip("Sampo", "sampo", "Ten")
)

val timeReferencesClips = listOf(
    FSLClip("Ngayon", "ngayon", "Today"),
    FSLClip("Bukas", "bukas", "Tomorrow"),
    FSLClip("Kahapon", "kahapon", "Yesterday")
)

val familyPeopleClips = listOf(
    FSLClip("Ama", "ama", "Father"),
    FSLClip("Ina", "ina", "Mother"),
    FSLClip("Anak na Lalaki", "anak_na_lalaki", "Son"),
    FSLClip("Anak na Babae", "anak_na_babae", "Daughter"),
    FSLClip("Lolo", "lolo", "Grandfather"),
    FSLClip("Lola", "lola", "Grandmother"),
    FSLClip("Tiyo", "tiyo", "Uncle"),
    FSLClip("Tiya", "tiya", "Auntie"),
    FSLClip("Pinsan", "pinsan", "Cousin"),
    FSLClip("Magulang", "magulang", "Parents"),
    FSLClip("Lalaki", "lalaki", "Boy/Man"),
    FSLClip("Babae", "babae", "Girl/Woman")
)

val monthsClips = listOf(
    FSLClip("Enero", "enero", "January"),
    FSLClip("Pebrero", "pebrero", "February"),
    FSLClip("Marso", "marso", "March"),
    FSLClip("Abril", "abril", "April"),
    FSLClip("Mayo", "mayo", "May"),
    FSLClip("Hunyo", "hunyo", "June"),
    FSLClip("Hulyo", "hulyo", "July"),
    FSLClip("Agosto", "agosto", "August"),
    FSLClip("Setyembre", "setyembre", "September"),
    FSLClip("Oktubre", "oktubre", "October"),
    FSLClip("Nobyembre", "nobyembre", "November"),
    FSLClip("Disyembre", "disyembre", "December")
)

val daysOfWeekClips = listOf(
    FSLClip("Lunes", "lunes", "Monday"),
    FSLClip("Martes", "martes", "Tuesday"),
    FSLClip("Miyerkules", "miyerkules", "Wednesday"),
    FSLClip("Huwebes", "huwebes", "Thursday"),
    FSLClip("Biyernes", "biyernes", "Friday"),
    FSLClip("Sabado", "sabado", "Saturday"),
    FSLClip("Linggo", "linggo", "Sunday")
)

val disabilityConditionsClips = listOf(
    FSLClip("Bingi", "bingi", "Deaf"),
    FSLClip("Mahina ang Pandinig", "mahina_ang_pandinig", "Hard of Hearing"),
    FSLClip("Taong Nasa Wheelchair", "taong_nasa_wheelchair", "Wheelchair Person"),
    FSLClip("Bulag", "bulag", "Blind"),
    FSLClip("Bingi-Bulag", "bingi_bulag", "Deaf Blind"),
    FSLClip("Kasal", "kasal", "Married")
)

val colorsClips = listOf(
    FSLClip("Asul", "asul", "Blue"),
    FSLClip("Berde", "berde", "Green"),
    FSLClip("Pula", "pula", "Red"),
    FSLClip("Kayumanggi", "kayumanggi", "Brown"),
    FSLClip("Itim", "itim", "Black"),
    FSLClip("Puti", "puti", "White"),
    FSLClip("Dilaw", "dilaw", "Yellow"),
    FSLClip("Kahelya", "kahelya", "Orange"),
    FSLClip("Abo", "abo", "Gray"),
    FSLClip("Rosa", "rosa", "Pink"),
    FSLClip("Ube/Lila", "ube_lila", "Violet"),
    FSLClip("Maliwanag", "maliwanag", "Light"),
    FSLClip("Madilim", "madilim", "Dark")
)

val foodDrinksClips = listOf(
    FSLClip("Tinapay", "tinapay", "Bread"),
    FSLClip("Itlog", "itlog", "Egg"),
    FSLClip("Isda", "isda", "Fish"),
    FSLClip("Karne", "karne", "Meat"),
    FSLClip("Manok", "manok", "Chicken"),
    FSLClip("Spaghetti", "spaghetti", "Spaghetti"),
    FSLClip("Kanin", "kanin", "Rice"),
    FSLClip("Longganisa", "longganisa", "Longanisa"),
    FSLClip("Hipon", "hipon", "Shrimp"),
    FSLClip("Alimango", "alimango", "Crab"),
    FSLClip("Mainit", "mainit", "Hot"),
    FSLClip("Malamig", "malamig", "Cold"),
    FSLClip("Juice", "juice", "Juice"),
    FSLClip("Gatas", "gatas", "Milk"),
    FSLClip("Kape", "kape", "Coffee"),
    FSLClip("Tsaa", "tsaa", "Tea"),
    FSLClip("Serbesa", "serbesa", "Beer"),
    FSLClip("Alak", "alak", "Wine"),
    FSLClip("Asukal", "asukal", "Sugar"),
    FSLClip("Walang Asukal", "walang_asukal", "No Sugar")
)

enum class LearnFSLState { CATEGORY, LIST, DETAIL }

@Composable
fun LearnFSLScreen(onBack: () -> Unit = {}) {
    var state by remember { mutableStateOf(LearnFSLState.CATEGORY) }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedClip by remember { mutableStateOf<FSLClip?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SenyasColors.Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
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
                IconButton(
                    onClick = {
                        when (state) {
                            LearnFSLState.DETAIL -> state = LearnFSLState.LIST
                            LearnFSLState.LIST -> state = LearnFSLState.CATEGORY
                            LearnFSLState.CATEGORY -> onBack()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = SenyasColors.OnSurface
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        when (state) {
                            LearnFSLState.CATEGORY -> "Learn FSL"
                            LearnFSLState.LIST -> selectedCategory
                            LearnFSLState.DETAIL -> selectedClip?.title ?: ""
                        },
                        style = MaterialTheme.typography.titleLarge,
                        color = SenyasColors.OnSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        when (state) {
                            LearnFSLState.CATEGORY -> "Choose a category to start learning"
                            LearnFSLState.LIST -> "Select a phrase to learn"
                            LearnFSLState.DETAIL -> selectedClip?.description ?: ""
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = SenyasColors.OnSurfaceVariant
                    )
                }
            }

            when (state) {
                LearnFSLState.CATEGORY -> {
                    // Category Selection
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            "Choose a Category",
                            style = MaterialTheme.typography.titleLarge,
                            color = SenyasColors.OnSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Select a category to start learning Filipino Sign Language",
                            style = MaterialTheme.typography.bodyLarge,
                            color = SenyasColors.OnSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        // Category Cards
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            item {
                                ModernCategoryCard(
                                    title = "Pagbati",
                                    subtitle = "Greetings & Basic Phrases",
                                    icon = Icons.Default.WavingHand,
                                    itemCount = pagbatiClips.size,
                                    onClick = {
                                        selectedCategory = "Pagbati"
                                        state = LearnFSLState.LIST
                                    }
                                )
                            }
                            
                            item {
                                ModernCategoryCard(
                                    title = "Numero",
                                    subtitle = "Numbers",
                                    icon = Icons.Default.Numbers,
                                    itemCount = numeroClips.size,
                                    onClick = {
                                        selectedCategory = "Numero"
                                        state = LearnFSLState.LIST
                                    }
                                )
                            }
                            
                            item {
                                ModernCategoryCard(
                                    title = "Oras",
                                    subtitle = "Time References",
                                    icon = Icons.Default.Schedule,
                                    itemCount = timeReferencesClips.size,
                                    onClick = {
                                        selectedCategory = "Oras"
                                        state = LearnFSLState.LIST
                                    }
                                )
                            }
                            
                            item {
                                ModernCategoryCard(
                                    title = "Pamilya",
                                    subtitle = "Family & People",
                                    icon = Icons.Default.People,
                                    itemCount = familyPeopleClips.size,
                                    onClick = {
                                        selectedCategory = "Pamilya"
                                        state = LearnFSLState.LIST
                                    }
                                )
                            }
                            
                            item {
                                ModernCategoryCard(
                                    title = "Buwan",
                                    subtitle = "Months",
                                    icon = Icons.Default.CalendarMonth,
                                    itemCount = monthsClips.size,
                                    onClick = {
                                        selectedCategory = "Buwan"
                                        state = LearnFSLState.LIST
                                    }
                                )
                            }
                            
                            item {
                                ModernCategoryCard(
                                    title = "Araw",
                                    subtitle = "Days of the Week",
                                    icon = Icons.Default.CalendarToday,
                                    itemCount = daysOfWeekClips.size,
                                    onClick = {
                                        selectedCategory = "Araw"
                                        state = LearnFSLState.LIST
                                    }
                                )
                            }
                            
                            item {
                                ModernCategoryCard(
                                    title = "Kalagayan",
                                    subtitle = "Disability & Conditions",
                                    icon = Icons.Default.Accessibility,
                                    itemCount = disabilityConditionsClips.size,
                                    onClick = {
                                        selectedCategory = "Kalagayan"
                                        state = LearnFSLState.LIST
                                    }
                                )
                            }
                            
                            item {
                                ModernCategoryCard(
                                    title = "Kulay",
                                    subtitle = "Colors",
                                    icon = Icons.Default.Palette,
                                    itemCount = colorsClips.size,
                                    onClick = {
                                        selectedCategory = "Kulay"
                                        state = LearnFSLState.LIST
                                    }
                                )
                            }
                            
                            item {
                                ModernCategoryCard(
                                    title = "Pagkain",
                                    subtitle = "Food & Drinks",
                                    icon = Icons.Default.Restaurant,
                                    itemCount = foodDrinksClips.size,
                                    onClick = {
                                        selectedCategory = "Pagkain"
                                        state = LearnFSLState.LIST
                                    }
                                )
                            }
                        }
                    }
                }

                LearnFSLState.LIST -> {
                    // Phrase List
                    val clips = when (selectedCategory) {
                        "Pagbati" -> pagbatiClips
                        "Numero" -> numeroClips
                        "Oras" -> timeReferencesClips
                        "Pamilya" -> familyPeopleClips
                        "Buwan" -> monthsClips
                        "Araw" -> daysOfWeekClips
                        "Kalagayan" -> disabilityConditionsClips
                        "Kulay" -> colorsClips
                        "Pagkain" -> foodDrinksClips
                        else -> emptyList()
                    }

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(clips) { clip ->
                            ModernPhraseListItem(
                                title = clip.title,
                                description = clip.description,
                                onClick = {
                                    selectedClip = clip
                                    state = LearnFSLState.DETAIL
                                }
                            )
                        }
                    }
                }

                LearnFSLState.DETAIL -> {
                    // Detail View with Video
                    selectedClip?.let { clip ->
                        ModernClipDetail(
                            clip = clip,
                            onBack = { state = LearnFSLState.LIST }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ModernCategoryCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    itemCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SenyasColors.Surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SenyasColors.Primary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = SenyasColors.OnSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = SenyasColors.OnSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "$itemCount phrases",
                style = MaterialTheme.typography.bodySmall,
                color = SenyasColors.Primary,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ModernPhraseListItem(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
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
            // Play Icon
            Icon(
                imageVector = Icons.Default.PlayCircleOutline,
                contentDescription = null,
                tint = SenyasColors.Primary,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = SenyasColors.OnSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SenyasColors.OnSurfaceVariant
                )
            }
            
            // Arrow Icon
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = SenyasColors.OnSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun ModernClipDetail(
    clip: FSLClip,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            playWhenReady = true
        }
    }

    LaunchedEffect(clip) {
        try {
            val resourceName = "learn_${clip.rawFileName}".replace(".mp4", "").lowercase()
            val resourceId = context.resources.getIdentifier(resourceName, "raw", context.packageName)
            
            if (resourceId != 0) {
                val uri = Uri.parse("android.resource://${context.packageName}/$resourceId")
                player.setMediaItem(MediaItem.fromUri(uri))
                player.prepare()
                player.playWhenReady = true
            }
        } catch (e: Exception) {
            // Handle error silently or add logging if needed
        }
    }

    DisposableEffect(Unit) {
        onDispose { player.release() }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Video Player
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = SenyasColors.Surface
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            ) {
                AndroidView(
                    factory = {
                        PlayerView(it).apply {
                            this.player = player
                            useController = true
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Clip Information
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = SenyasColors.Surface
            )
                    ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                Text(
                    text = clip.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = SenyasColors.OnSurface,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = clip.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = SenyasColors.OnSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                // Practice Button
                Button(
                    onClick = {
                        player.seekTo(0)
                        player.playWhenReady = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SenyasColors.Primary,
                        contentColor = SenyasColors.OnPrimary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Practice Again")
                }
            }
        }
    }
}

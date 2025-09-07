package com.example.senyas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.senyas.ui.theme.SenyasColors

@Composable
fun AboutScreen(onBack: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SenyasColors.Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp),
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
                Text(
                    text = "About Senyas",
                    style = MaterialTheme.typography.titleLarge,
                    color = SenyasColors.OnSurface,
                    fontWeight = FontWeight.Bold
                )
            }

            // App Logo and Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SenyasColors.Surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.senyas_w),
                        contentDescription = "Senyas Logo",
                        modifier = Modifier.size(80.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "SENYAS",
                        style = MaterialTheme.typography.headlineSmall,
                        color = SenyasColors.OnSurface,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Filipino Sign Language Translator",
                        style = MaterialTheme.typography.bodyLarge,
                        color = SenyasColors.OnSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Version Beta 0.1.0",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SenyasColors.Primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // About Description
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SenyasColors.Surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        "What is Senyas?",
                        style = MaterialTheme.typography.titleMedium,
                        color = SenyasColors.OnSurface,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Senyas is a Filipino Sign Language (FSL) translator app designed to bridge communication gaps between the deaf and hearing communities in the Philippines. Our mission is to make FSL more accessible and promote inclusive communication.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SenyasColors.OnSurfaceVariant,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Features",
                        style = MaterialTheme.typography.titleMedium,
                        color = SenyasColors.OnSurface,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FeatureItem(
                        icon = Icons.Default.Translate,
                        title = "Text to FSL Translation",
                        description = "Convert Filipino text to sign language videos"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FeatureItem(
                        icon = Icons.Default.History,
                        title = "Translation History",
                        description = "Keep track of your previous translations"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FeatureItem(
                        icon = Icons.Default.Bookmark,
                        title = "Save Favorites",
                        description = "Bookmark frequently used translations"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FeatureItem(
                        icon = Icons.Default.School,
                        title = "Learn FSL",
                        description = "Interactive lessons for learning sign language"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Mission Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SenyasColors.Primary.copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = SenyasColors.Primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Our Mission",
                            style = MaterialTheme.typography.titleMedium,
                            color = SenyasColors.OnSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "To create an inclusive society where deaf and hearing individuals can communicate seamlessly through technology, promoting understanding and breaking down communication barriers.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SenyasColors.OnSurfaceVariant,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Development Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SenyasColors.Surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        "Development",
                        style = MaterialTheme.typography.titleMedium,
                        color = SenyasColors.OnSurface,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Senyas is developed with care and dedication to serve the Filipino deaf community. This app is built using modern Android development practices and is continuously improved based on user feedback.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SenyasColors.OnSurfaceVariant,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "© 2024 Senyas Team. All rights reserved.",
                        style = MaterialTheme.typography.bodySmall,
                        color = SenyasColors.OnSurfaceLight,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun FeatureItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = SenyasColors.Primary,
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = SenyasColors.OnSurface,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = SenyasColors.OnSurfaceVariant
            )
        }
    }
}

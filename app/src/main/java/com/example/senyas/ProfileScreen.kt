package com.example.senyas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.senyas.ui.theme.SenyasColors
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProfileScreen(onBack: () -> Unit = {}) {
    val auth = Firebase.auth
    val currentUser = auth.currentUser

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
                    text = "Profile",
                    style = MaterialTheme.typography.titleLarge,
                    color = SenyasColors.OnSurface,
                    fontWeight = FontWeight.Bold
                )
            }

            // Profile Card
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
                    // Profile Avatar
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(SenyasColors.Primary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = SenyasColors.Primary,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // User Email
                    Text(
                        text = currentUser?.email ?: "No email",
                        style = MaterialTheme.typography.titleMedium,
                        color = SenyasColors.OnSurface,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Member Since
                    Text(
                        text = "Member since ${
                            currentUser?.metadata?.creationTimestamp?.let {
                                SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(Date(it))
                            } ?: "Unknown"
                        }",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SenyasColors.OnSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Account Information
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
                        "Account Information",
                        style = MaterialTheme.typography.titleMedium,
                        color = SenyasColors.OnSurface,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email Row
                    ProfileInfoRow(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = currentUser?.email ?: "Not available"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // User ID Row
                    ProfileInfoRow(
                        icon = Icons.Default.Key,
                        label = "User ID",
                        value = currentUser?.uid?.take(8) + "..." ?: "Not available"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Email Verified Row
                    ProfileInfoRow(
                        icon = if (currentUser?.isEmailVerified == true) Icons.Default.Verified else Icons.Default.Warning,
                        label = "Email Status",
                        value = if (currentUser?.isEmailVerified == true) "Verified" else "Not verified"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Last Sign In
                    ProfileInfoRow(
                        icon = Icons.Default.Schedule,
                        label = "Last Sign In",
                        value = currentUser?.metadata?.lastSignInTimestamp?.let {
                            SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(it))
                        } ?: "Unknown"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Statistics Card
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
                        "Usage Statistics",
                        style = MaterialTheme.typography.titleMedium,
                        color = SenyasColors.OnSurface,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatCard(
                            icon = Icons.Default.History,
                            label = "Translations",
                            value = "0", // TODO: Get from history
                            modifier = Modifier.weight(1f)
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        StatCard(
                            icon = Icons.Default.Bookmark,
                            label = "Saved",
                            value = "0", // TODO: Get from saved
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
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
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = SenyasColors.OnSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = SenyasColors.OnSurface,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun StatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = SenyasColors.Primary.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SenyasColors.Primary,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = SenyasColors.OnSurface,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = SenyasColors.OnSurfaceVariant
            )
        }
    }
}

package com.example.senyas.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.senyas.ui.theme.SenyasColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class NotificationType {
    SUCCESS, ERROR, INFO
}

data class NotificationData(
    val message: String,
    val type: NotificationType = NotificationType.INFO,
    val duration: Long = 3000L
)

@Composable
fun ModernNotificationHost(
    notificationState: NotificationState,
    modifier: Modifier = Modifier
) {
    val currentNotification by notificationState.currentNotification.collectAsState()
    
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visible = currentNotification != null,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeIn(
                animationSpec = tween(300)
            ),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(200)
            ) + fadeOut(
                animationSpec = tween(200)
            )
        ) {
            currentNotification?.let { notification ->
                ModernNotificationCard(
                    notification = notification,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun ModernNotificationCard(
    notification: NotificationData,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, contentColor, icon) = when (notification.type) {
        NotificationType.SUCCESS -> Triple(
            Color(0xFF10B981), // Emerald-500
            Color.White,
            Icons.Default.CheckCircle
        )
        NotificationType.ERROR -> Triple(
            Color(0xFFEF4444), // Red-500
            Color.White,
            Icons.Default.Error
        )
        NotificationType.INFO -> Triple(
            Color(0xFF3B82F6), // Blue-500
            Color.White,
            Icons.Default.Info
        )
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )
            
            Text(
                text = notification.message,
                color = contentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

class NotificationState {
    private val _currentNotification = MutableStateFlow<NotificationData?>(null)
    val currentNotification = _currentNotification.asStateFlow()
    
    suspend fun showNotification(notification: NotificationData) {
        _currentNotification.value = notification
        delay(notification.duration)
        _currentNotification.value = null
    }
    
    suspend fun showSuccess(message: String, duration: Long = 3000L) {
        showNotification(NotificationData(message, NotificationType.SUCCESS, duration))
    }
    
    suspend fun showError(message: String, duration: Long = 4000L) {
        showNotification(NotificationData(message, NotificationType.ERROR, duration))
    }
    
    suspend fun showInfo(message: String, duration: Long = 3000L) {
        showNotification(NotificationData(message, NotificationType.INFO, duration))
    }
    
    fun dismiss() {
        _currentNotification.value = null
    }
}

@Composable
fun rememberNotificationState(): NotificationState {
    return remember { NotificationState() }
}

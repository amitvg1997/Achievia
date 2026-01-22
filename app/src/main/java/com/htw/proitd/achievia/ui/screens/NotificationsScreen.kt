package com.htw.proitd.achievia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.htw.proitd.achievia.ui.theme.Orange500
import com.htw.proitd.achievia.data.notifications.INotificationService
import com.htw.proitd.achievia.data.notifications.MockNotificationService
import com.htw.proitd.achievia.data.notifications.Notification as ServiceNotification
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import java.text.SimpleDateFormat
import java.util.*

data class NotificationUi(
    val id: String,
    val initials: String,
    val name: String,
    val message: String,
    val timeAgo: String,
    val type: NotificationType
)

enum class NotificationType {
    FriendRequest,
    SharedGoal,
    SharedTask
}

/**
 * Convert service Notification to UI NotificationUi
 */
private fun ServiceNotification.toNotificationUi(): NotificationUi {
    val timeAgo = formatTimeAgo(timestamp)
    val uiType = when (type) {
        com.htw.proitd.achievia.data.notifications.NotificationType.FriendRequest -> NotificationType.FriendRequest
        com.htw.proitd.achievia.data.notifications.NotificationType.SharedGoal -> NotificationType.SharedGoal
        com.htw.proitd.achievia.data.notifications.NotificationType.SharedTask -> NotificationType.SharedTask
        else -> NotificationType.SharedGoal // Default fallback
    }
    return NotificationUi(
        id = id,
        initials = userInitials,
        name = userName,
        message = message,
        timeAgo = timeAgo,
        type = uiType
    )
}

/**
 * Format timestamp to "time ago" string
 */
private fun formatTimeAgo(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        days > 0 -> "$days day${if (days > 1) "s" else ""} ago"
        hours > 0 -> "$hours hour${if (hours > 1) "s" else ""} ago"
        minutes > 0 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
        else -> "Just now"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    notificationService: INotificationService = MockNotificationService,
    onNavigateBack: () -> Unit
) {
    val notificationsFlow = notificationService.notifications.collectAsState()
    val notifications = notificationsFlow.value.map { it.toNotificationUi() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Notifications", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(
                            "Stay updated with your activities",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Orange500)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F7FB))
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notifications, key = { it.id }) { notification ->
                    NotificationCard(
                        notification = notification,
                        notificationService = notificationService,
                        onDismiss = {
                            scope.launch {
                                notificationService.deleteNotification(notification.id)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(
    notification: NotificationUi,
    notificationService: INotificationService = MockNotificationService,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Orange500),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        notification.initials,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        when (notification.type) {
                            NotificationType.FriendRequest -> Icon(
                                imageVector = Icons.Default.GroupAdd,
                                contentDescription = null,
                                tint = Orange500,
                                modifier = Modifier.size(18.dp)
                            )
                            else -> Icon(
                                imageVector = Icons.Default.RadioButtonChecked,
                                contentDescription = null,
                                tint = Orange500,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            notification.name,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        notification.message,
                        fontSize = 14.sp,
                        color = Color(0xFF4B5563)
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint = Color(0xFF9CA3AF)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                notification.timeAgo,
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF)
            )
            Spacer(modifier = Modifier.height(12.dp))
            when (notification.type) {
                NotificationType.FriendRequest -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                scope.launch {
                                    notificationService.acceptFriendRequest(notification.id)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Orange500,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Accept")
                        }
                        OutlinedButton(
                            onClick = {
                                scope.launch {
                                    notificationService.declineFriendRequest(notification.id)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Decline")
                        }
                    }
                }
                else -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                scope.launch {
                                    notificationService.markAsRead(notification.id)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Orange500,
                                contentColor = Color.White
                            )
                        ) {
                            Text("View")
                        }
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(48.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Dismiss"
                            )
                        }
                    }
                }
            }
        }
    }
}



package com.htw.proitd.achievia.data.notifications

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

/**
 * Mock implementation of INotificationService.
 * Uses in-memory storage with StateFlow for reactive updates.
 */
object MockNotificationService : INotificationService {
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    private val _unreadCount = MutableStateFlow(0)
    private val readNotificationIds = mutableSetOf<String>()

    override val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()
    override val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    init {
        // Seed with some dummy notifications
        val now = System.currentTimeMillis()
        _notifications.value = listOf(
            Notification(
                id = "notif1",
                userId = "user1",
                userName = "Alex Martinez",
                userInitials = "AM",
                message = "sent you a friend request",
                timestamp = now - (2 * 60 * 60 * 1000), // 2 hours ago
                type = NotificationType.FriendRequest,
                relatedId = "user1"
            ),
            Notification(
                id = "notif2",
                userId = "user2",
                userName = "Sarah Johnson",
                userInitials = "SJ",
                message = "added you to a shared goal\n\"Complete React Course\"",
                timestamp = now - (5 * 60 * 60 * 1000), // 5 hours ago
                type = NotificationType.SharedGoal,
                relatedId = "goal1"
            ),
            Notification(
                id = "notif3",
                userId = "user3",
                userName = "Mike Chen",
                userInitials = "MC",
                message = "shared a task with you\n\"Build Project Dashboard\"",
                timestamp = now - (24 * 60 * 60 * 1000), // 1 day ago
                type = NotificationType.SharedTask,
                relatedId = "task1"
            )
        )
        updateUnreadCount()
    }

    override suspend fun markAsRead(notificationId: String) {
        delay(100)
        readNotificationIds.add(notificationId)
        updateUnreadCount()
    }

    override suspend fun markAllAsRead() {
        delay(200)
        _notifications.value.forEach { readNotificationIds.add(it.id) }
        updateUnreadCount()
    }

    override suspend fun deleteNotification(notificationId: String) {
        delay(100)
        _notifications.update { it.filter { notif -> notif.id != notificationId } }
        readNotificationIds.remove(notificationId)
        updateUnreadCount()
    }

    override suspend fun acceptFriendRequest(notificationId: String): Boolean {
        delay(300)
        val notification = _notifications.value.find { it.id == notificationId }
        if (notification == null || notification.type != NotificationType.FriendRequest) {
            return false
        }

        // In real app, would call friend service to accept request
        // For now, just mark as read and update message
        _notifications.update { list ->
            list.map { notif ->
                if (notif.id == notificationId) {
                    notif.copy(message = "Friend request accepted")
                } else {
                    notif
                }
            }
        }
        markAsRead(notificationId)
        return true
    }

    override suspend fun declineFriendRequest(notificationId: String) {
        delay(200)
        deleteNotification(notificationId)
    }

    private fun updateUnreadCount() {
        _unreadCount.value = _notifications.value.count { it.id !in readNotificationIds }
    }
}


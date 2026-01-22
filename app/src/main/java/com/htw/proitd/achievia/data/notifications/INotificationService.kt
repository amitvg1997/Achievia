package com.htw.proitd.achievia.data.notifications

import kotlinx.coroutines.flow.StateFlow

/**
 * Notification data model
 */
data class Notification(
    val id: String,
    val userId: String,
    val userName: String,
    val userInitials: String,
    val message: String,
    val timestamp: Long,
    val type: NotificationType,
    val relatedId: String? = null // ID of related goal/task/friend request
)

/**
 * Type of notification
 */
enum class NotificationType {
    FriendRequest,
    SharedGoal,
    SharedTask,
    GoalUpdate,
    TaskCompleted
}

/**
 * Interface for notification operations.
 * Provides abstraction for notification management.
 */
interface INotificationService {
    /**
     * Observable flow of all notifications
     */
    val notifications: StateFlow<List<Notification>>

    /**
     * Get unread notification count
     */
    val unreadCount: StateFlow<Int>

    /**
     * Mark a notification as read
     * @param notificationId The ID of the notification
     */
    suspend fun markAsRead(notificationId: String)

    /**
     * Mark all notifications as read
     */
    suspend fun markAllAsRead()

    /**
     * Delete a notification
     * @param notificationId The ID of the notification to delete
     */
    suspend fun deleteNotification(notificationId: String)

    /**
     * Accept a friend request notification
     * @param notificationId The ID of the friend request notification
     * @return true if successful, false otherwise
     */
    suspend fun acceptFriendRequest(notificationId: String): Boolean

    /**
     * Decline a friend request notification
     * @param notificationId The ID of the friend request notification
     */
    suspend fun declineFriendRequest(notificationId: String)
}


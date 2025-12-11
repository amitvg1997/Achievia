package com.htw.proitd.achievia.data

/**
 * Notification routing (e.g., push, email, in-app).
 */
interface NotificationService {
    suspend fun notifyGoalShared(goalId: String, recipientUserIds: List<String>)
    suspend fun notifyTaskUpdated(goalId: String, taskId: String, recipientUserIds: List<String>)
    suspend fun notifyFriendRequest(senderUserId: String, recipientUserId: String)
}

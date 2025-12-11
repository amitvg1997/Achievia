package com.htw.proitd.achievia.data

/**
 * Stub notification service; replace with FCM/Email integration later.
 */
class NoOpNotificationService : NotificationService {
    override suspend fun notifyGoalShared(goalId: String, recipientUserIds: List<String>) {
        // Intentionally no-op in memory.
    }

    override suspend fun notifyTaskUpdated(goalId: String, taskId: String, recipientUserIds: List<String>) {
        // Intentionally no-op in memory.
    }

    override suspend fun notifyFriendRequest(senderUserId: String, recipientUserId: String) {
        // Intentionally no-op in memory.
    }
}

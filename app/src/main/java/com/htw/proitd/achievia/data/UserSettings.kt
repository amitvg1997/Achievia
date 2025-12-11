package com.htw.proitd.achievia.data

/**
 * Simple container for per-user preferences.
 * Extend as new settings are introduced.
 */
data class UserSettings(
    val darkModeEnabled: Boolean = false,
    val pushNotificationsEnabled: Boolean = true,
    val emailNotificationsEnabled: Boolean = false
)

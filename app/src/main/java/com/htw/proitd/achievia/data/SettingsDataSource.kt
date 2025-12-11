package com.htw.proitd.achievia.data

import kotlinx.coroutines.flow.Flow

/**
 * User-configurable settings such as theme and notification toggles.
 */
interface SettingsDataSource {
    fun observeSettings(userId: String): Flow<UserSettings>
    suspend fun updateSettings(userId: String, settings: UserSettings)
}

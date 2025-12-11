package com.htw.proitd.achievia.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * In-memory user settings storage.
 */
class InMemorySettingsDataSource(
    private val store: InMemoryBackendStore = InMemoryBackendStore
) : SettingsDataSource {

    override fun observeSettings(userId: String): Flow<UserSettings> {
        // Emits user settings, defaulting when none exist yet.
        return store.settingsState.map { it[userId] ?: UserSettings() }
    }

    override suspend fun updateSettings(userId: String, settings: UserSettings) {
        store.settingsState.update { current -> current + (userId to settings) }
    }
}

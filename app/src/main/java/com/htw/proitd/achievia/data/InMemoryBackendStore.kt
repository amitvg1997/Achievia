package com.htw.proitd.achievia.data

import com.htw.proitd.achievia.model.Goal
import com.htw.proitd.achievia.model.Task
import com.htw.proitd.achievia.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

/**
 * Shared in-memory backing store used by the demo implementations.
 * This keeps goal, user, and settings state in one place so flows stay consistent.
 */
object InMemoryBackendStore {
    val goalsState = MutableStateFlow(seedGoals())
    val usersByEmail = mutableMapOf<String, User>()
    val currentUserState = MutableStateFlow<User?>(null)
    val friendsState = MutableStateFlow<Map<String, List<User>>>(emptyMap())
    val settingsState = MutableStateFlow<Map<String, UserSettings>>(emptyMap())

    private fun seedGoals(): List<Goal> {
        val sampleTasks = listOf(
            Task(
                id = UUID.randomUUID().toString(),
                title = "Learn Hooks",
                description = "Complete useState, useEffect",
                allocatedHours = 10f,
                loggedHours = 7.5f
            ),
            Task(
                id = UUID.randomUUID().toString(),
                title = "Build Dashboard",
                description = "Create responsive admin dashboard",
                allocatedHours = 20f,
                loggedHours = 15f
            )
        )
        return listOf(
            Goal(
                title = "Complete React Course",
                description = "Finish all modules and build final project",
                progress = 75,
                sharedWith = listOf("Sarah Johnson"),
                tasks = sampleTasks
            ),
            Goal(
                title = "Morning Workout",
                description = "Exercise 5 days a week for 30 minutes",
                progress = 60
            )
        )
    }
}

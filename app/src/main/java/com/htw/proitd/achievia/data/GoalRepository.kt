package com.htw.proitd.achievia.data

import com.htw.proitd.achievia.model.Goal
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

/**
 * Simple in-memory repository facade used by the UI layer.
 *
 * This mirrors the previous Compose-facing API (exposing a StateFlow and
 * synchronous CRUD helpers) while reusing the shared in-memory store. It keeps
 * the screens compiling until a proper DI setup is added.
 */
object GoalRepository {
    private val store = InMemoryBackendStore

    val goals: StateFlow<List<Goal>> = store.goalsState

    fun getGoal(goalId: String): Goal? = store.goalsState.value.find { it.id == goalId }

    fun addGoal(goal: Goal) {
        store.goalsState.update { current ->
            val goalWithId = if (goal.id.isNotEmpty()) goal else goal.copy(id = UUID.randomUUID().toString())
            current + goalWithId
        }
    }

    fun updateGoal(goal: Goal) {
        store.goalsState.update { current ->
            current.map { if (it.id == goal.id) goal else it }
        }
    }

    fun deleteGoal(goalId: String) {
        store.goalsState.update { current ->
            current.filterNot { it.id == goalId }
        }
    }
}

package com.htw.proitd.achievia.data

import com.htw.proitd.achievia.model.Goal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.UUID

/**
 * In-memory implementation of backend goal operations.
 * Replace with Firebase-backed implementation later.
 */
class InMemoryGoalDataSource(
    private val store: InMemoryBackendStore = InMemoryBackendStore
) : GoalDataSource {

    override fun observeGoals(ownerUserId: String): Flow<List<Goal>> {
        // Emits whenever the goal list changes; owner filtering can be added later.
        return store.goalsState.map { it }
    }

    override suspend fun getGoal(goalId: String): Goal? {
        return store.goalsState.value.find { it.id == goalId }
    }

    override suspend fun createGoal(goal: Goal): Goal {
        val goalWithId = if (goal.id.isNotEmpty()) goal else goal.copy(id = UUID.randomUUID().toString())
        store.goalsState.update { it + goalWithId }
        return goalWithId
    }

    override suspend fun updateGoal(goal: Goal) {
        store.goalsState.update { goals ->
            goals.map { if (it.id == goal.id) goal else it }
        }
    }

    override suspend fun deleteGoal(goalId: String) {
        store.goalsState.update { goals ->
            goals.filter { it.id != goalId }
        }
    }
}

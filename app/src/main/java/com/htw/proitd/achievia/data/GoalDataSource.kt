package com.htw.proitd.achievia.data

import com.htw.proitd.achievia.model.Goal
import kotlinx.coroutines.flow.Flow

/**
 * Backend-facing goal operations (e.g., Firebase implementation).
 */
interface GoalDataSource {
    fun observeGoals(ownerUserId: String): Flow<List<Goal>>
    suspend fun getGoal(goalId: String): Goal?
    suspend fun createGoal(goal: Goal): Goal
    suspend fun updateGoal(goal: Goal)
    suspend fun deleteGoal(goalId: String)
}

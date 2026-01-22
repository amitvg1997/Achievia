package com.htw.proitd.achievia.data

import com.htw.proitd.achievia.model.Goal
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for goal repository operations.
 * Provides abstraction for goal data management.
 */
interface IGoalRepository {
    /**
     * Observable flow of all goals
     */
    val goals: StateFlow<List<Goal>>

    /**
     * Add a new goal
     * @param goal The goal to add
     */
    suspend fun addGoal(goal: Goal)

    /**
     * Update an existing goal
     * @param updatedGoal The updated goal
     */
    suspend fun updateGoal(updatedGoal: Goal)

    /**
     * Delete a goal by ID
     * @param goalId The ID of the goal to delete
     */
    suspend fun deleteGoal(goalId: String)

    /**
     * Get a goal by ID
     * @param goalId The ID of the goal to retrieve
     * @return The goal if found, null otherwise
     */
    suspend fun getGoal(goalId: String): Goal?
}


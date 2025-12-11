package com.htw.proitd.achievia.data

import com.htw.proitd.achievia.model.Goal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository interface the UI will depend on.
 * Can be backed by Firebase or any other source.
 */
interface GoalRepository {
    fun observeGoals(ownerUserId: String? = null): Flow<List<Goal>>
    suspend fun getGoal(goalId: String): Goal?
    suspend fun addGoal(goal: Goal)
    suspend fun updateGoal(goal: Goal)
    suspend fun deleteGoal(goalId: String)
}

/**
 * GoalRepository implementation that delegates to the backend data source.
 * This keeps UI depending on a single façade while allowing swap of data source.
 */
class GoalRepositoryImpl(
    private val dataSource: GoalDataSource
) : GoalRepository {

    override fun observeGoals(ownerUserId: String?): Flow<List<Goal>> {
        // Delegate to backend source; owner filtering defaults to all goals.
        return dataSource.observeGoals(ownerUserId ?: "")
    }

    override suspend fun getGoal(goalId: String): Goal? = dataSource.getGoal(goalId)

    override suspend fun addGoal(goal: Goal) {
        dataSource.createGoal(goal)
    }

    override suspend fun updateGoal(goal: Goal) {
        dataSource.updateGoal(goal)
    }

    override suspend fun deleteGoal(goalId: String) {
        dataSource.deleteGoal(goalId)
    }
}

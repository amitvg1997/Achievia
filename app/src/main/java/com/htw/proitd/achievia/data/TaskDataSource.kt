package com.htw.proitd.achievia.data

import com.htw.proitd.achievia.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Contract for task-level operations within a goal.
 */
interface TaskDataSource {
    fun observeTasks(goalId: String): Flow<List<Task>>
    suspend fun getTask(goalId: String, taskId: String): Task?
    suspend fun createTask(goalId: String, task: Task): Task
    suspend fun updateTask(goalId: String, task: Task)
    suspend fun deleteTask(goalId: String, taskId: String)
    suspend fun logHours(goalId: String, taskId: String, hoursToAdd: Float)
}

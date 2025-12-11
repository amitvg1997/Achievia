package com.htw.proitd.achievia.data

import com.htw.proitd.achievia.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.UUID

/**
 * In-memory task operations scoped to a goal.
 */
class InMemoryTaskDataSource(
    private val store: InMemoryBackendStore = InMemoryBackendStore
) : TaskDataSource {

    override fun observeTasks(goalId: String): Flow<List<Task>> {
        // Emits the current task list for the goal whenever goals change.
        return store.goalsState.map { goals -> goals.find { it.id == goalId }?.tasks.orEmpty() }
    }

    override suspend fun getTask(goalId: String, taskId: String): Task? {
        return store.goalsState.value.find { it.id == goalId }?.tasks?.find { it.id == taskId }
    }

    override suspend fun createTask(goalId: String, task: Task): Task {
        val taskWithId = if (task.id.isNotEmpty()) task else task.copy(id = UUID.randomUUID().toString())
        store.goalsState.update { goals ->
            goals.map { goal ->
                if (goal.id == goalId) goal.copy(tasks = goal.tasks + taskWithId) else goal
            }
        }
        return taskWithId
    }

    override suspend fun updateTask(goalId: String, task: Task) {
        store.goalsState.update { goals ->
            goals.map { goal ->
                if (goal.id == goalId) {
                    goal.copy(tasks = goal.tasks.map { if (it.id == task.id) task else it })
                } else goal
            }
        }
    }

    override suspend fun deleteTask(goalId: String, taskId: String) {
        store.goalsState.update { goals ->
            goals.map { goal ->
                if (goal.id == goalId) goal.copy(tasks = goal.tasks.filter { it.id != taskId }) else goal
            }
        }
    }

    override suspend fun logHours(goalId: String, taskId: String, hoursToAdd: Float) {
        store.goalsState.update { goals ->
            goals.map { goal ->
                if (goal.id == goalId) {
                    val updatedTasks = goal.tasks.map { task ->
                        if (task.id == taskId) task.copy(loggedHours = (task.loggedHours + hoursToAdd).coerceAtLeast(0f)) else task
                    }
                    goal.copy(tasks = updatedTasks)
                } else goal
            }
        }
    }
}

package com.htw.proitd.achievia.data.tasks

import com.htw.proitd.achievia.model.Task

/**
 * Result of task operations
 */
sealed class TaskResult {
    data class Success(val task: Task) : TaskResult()
    data class Error(val message: String) : TaskResult()
}

/**
 * Result of hours logging operations
 */
sealed class HoursLoggingResult {
    data class Success(val task: Task, val totalLoggedHours: Float) : HoursLoggingResult()
    data class Error(val message: String) : HoursLoggingResult()
}

/**
 * Interface for task management operations.
 * Provides abstraction for task CRUD operations and hours logging.
 */
interface ITaskService {
    /**
     * Add a task to a goal
     * @param goalId The ID of the goal to add the task to
     * @param task The task to add
     * @return TaskResult indicating success or failure
     */
    suspend fun addTaskToGoal(goalId: String, task: Task): TaskResult

    /**
     * Update an existing task
     * @param goalId The ID of the goal containing the task
     * @param task The updated task
     * @return TaskResult indicating success or failure
     */
    suspend fun updateTask(goalId: String, task: Task): TaskResult

    /**
     * Delete a task from a goal
     * @param goalId The ID of the goal containing the task
     * @param taskId The ID of the task to delete
     * @return true if successful, false otherwise
     */
    suspend fun deleteTask(goalId: String, taskId: String): Boolean

    /**
     * Get a task by ID
     * @param goalId The ID of the goal containing the task
     * @param taskId The ID of the task to retrieve
     * @return The task if found, null otherwise
     */
    suspend fun getTask(goalId: String, taskId: String): Task?

    /**
     * Get all tasks for a goal
     * @param goalId The ID of the goal
     * @return List of tasks for the goal
     */
    suspend fun getTasksForGoal(goalId: String): List<Task>

    /**
     * Log hours for a task
     * @param goalId The ID of the goal containing the task
     * @param taskId The ID of the task to log hours for
     * @param hours The number of hours to log
     * @return HoursLoggingResult indicating success or failure
     */
    suspend fun logHours(goalId: String, taskId: String, hours: Float): HoursLoggingResult

    /**
     * Update logged hours for a task (set absolute value)
     * @param goalId The ID of the goal containing the task
     * @param taskId The ID of the task
     * @param hours The new total logged hours value
     * @return HoursLoggingResult indicating success or failure
     */
    suspend fun updateLoggedHours(goalId: String, taskId: String, hours: Float): HoursLoggingResult

    /**
     * Mark a task as completed
     * @param goalId The ID of the goal containing the task
     * @param taskId The ID of the task to mark as completed
     * @return TaskResult indicating success or failure
     */
    suspend fun markTaskAsCompleted(goalId: String, taskId: String): TaskResult

    /**
     * Mark a task as incomplete
     * @param goalId The ID of the goal containing the task
     * @param taskId The ID of the task to mark as incomplete
     * @return TaskResult indicating success or failure
     */
    suspend fun markTaskAsIncomplete(goalId: String, taskId: String): TaskResult

    /**
     * Calculate task progress percentage
     * @param task The task to calculate progress for
     * @return Progress percentage (0-100)
     */
    fun calculateTaskProgress(task: Task): Int

    /**
     * Validate hours to log
     * @param task The task to validate hours for
     * @param hours The hours to validate
     * @return true if valid, false otherwise
     */
    fun validateHoursToLog(task: Task, hours: Float): Boolean
}


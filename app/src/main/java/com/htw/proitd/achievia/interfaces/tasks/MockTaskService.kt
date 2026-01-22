package com.htw.proitd.achievia.interfaces.tasks

import com.htw.proitd.achievia.interfaces.IGoalRepository
import com.htw.proitd.achievia.interfaces.GoalRepository
import com.htw.proitd.achievia.model.Task
import kotlinx.coroutines.delay

/**
 * Mock implementation of ITaskService.
 * Uses GoalRepository to manage tasks as part of goals.
 */
object MockTaskService : ITaskService {
    private val goalRepository: IGoalRepository = GoalRepository

    override suspend fun addTaskToGoal(goalId: String, task: Task): TaskResult {
        delay(200)
        
        val goal = goalRepository.getGoal(goalId)
        if (goal == null) {
            return TaskResult.Error("Goal not found")
        }

        // Check if task with same ID already exists
        if (goal.tasks.any { it.id == task.id }) {
            return TaskResult.Error("Task with this ID already exists")
        }

        val updatedGoal = goal.copy(tasks = goal.tasks + task)
        goalRepository.updateGoal(updatedGoal)
        
        return TaskResult.Success(task)
    }

    override suspend fun updateTask(goalId: String, task: Task): TaskResult {
        delay(200)
        
        val goal = goalRepository.getGoal(goalId)
        if (goal == null) {
            return TaskResult.Error("Goal not found")
        }

        val taskExists = goal.tasks.any { it.id == task.id }
        if (!taskExists) {
            return TaskResult.Error("Task not found")
        }

        val updatedTasks = goal.tasks.map { if (it.id == task.id) task else it }
        val updatedGoal = goal.copy(tasks = updatedTasks)
        goalRepository.updateGoal(updatedGoal)
        
        return TaskResult.Success(task)
    }

    override suspend fun deleteTask(goalId: String, taskId: String): Boolean {
        delay(200)
        
        val goal = goalRepository.getGoal(goalId)
        if (goal == null) {
            return false
        }

        val updatedTasks = goal.tasks.filter { it.id != taskId }
        val updatedGoal = goal.copy(tasks = updatedTasks)
        goalRepository.updateGoal(updatedGoal)
        
        return true
    }

    override suspend fun getTask(goalId: String, taskId: String): Task? {
        delay(100)
        
        val goal = goalRepository.getGoal(goalId)
        return goal?.tasks?.find { it.id == taskId }
    }

    override suspend fun getTasksForGoal(goalId: String): List<Task> {
        delay(100)
        
        val goal = goalRepository.getGoal(goalId)
        return goal?.tasks ?: emptyList()
    }

    override suspend fun logHours(goalId: String, taskId: String, hours: Float): HoursLoggingResult {
        delay(300)
        
        if (hours <= 0) {
            return HoursLoggingResult.Error("Hours must be greater than zero")
        }

        val goal = goalRepository.getGoal(goalId)
        if (goal == null) {
            return HoursLoggingResult.Error("Goal not found")
        }

        val task = goal.tasks.find { it.id == taskId }
        if (task == null) {
            return HoursLoggingResult.Error("Task not found")
        }

        if (!validateHoursToLog(task, hours)) {
            return HoursLoggingResult.Error("Cannot log more hours than allocated")
        }

        val updatedTask = task.copy(loggedHours = task.loggedHours + hours)
        val updatedTasks = goal.tasks.map { if (it.id == taskId) updatedTask else it }
        
        // Recalculate goal progress based on task completion
        val updatedGoal = goal.copy(
            tasks = updatedTasks,
            progress = calculateGoalProgress(updatedTasks)
        )
        
        goalRepository.updateGoal(updatedGoal)
        
        return HoursLoggingResult.Success(updatedTask, updatedTask.loggedHours)
    }

    override suspend fun updateLoggedHours(goalId: String, taskId: String, hours: Float): HoursLoggingResult {
        delay(300)
        
        if (hours < 0) {
            return HoursLoggingResult.Error("Hours cannot be negative")
        }

        val goal = goalRepository.getGoal(goalId)
        if (goal == null) {
            return HoursLoggingResult.Error("Goal not found")
        }

        val task = goal.tasks.find { it.id == taskId }
        if (task == null) {
            return HoursLoggingResult.Error("Task not found")
        }

        if (hours > task.allocatedHours) {
            return HoursLoggingResult.Error("Cannot set logged hours greater than allocated hours")
        }

        val updatedTask = task.copy(loggedHours = hours)
        val updatedTasks = goal.tasks.map { if (it.id == taskId) updatedTask else it }
        
        // Recalculate goal progress
        val updatedGoal = goal.copy(
            tasks = updatedTasks,
            progress = calculateGoalProgress(updatedTasks)
        )
        
        goalRepository.updateGoal(updatedGoal)
        
        return HoursLoggingResult.Success(updatedTask, updatedTask.loggedHours)
    }

    override suspend fun markTaskAsCompleted(goalId: String, taskId: String): TaskResult {
        delay(200)
        
        val goal = goalRepository.getGoal(goalId)
        if (goal == null) {
            return TaskResult.Error("Goal not found")
        }

        val task = goal.tasks.find { it.id == taskId }
        if (task == null) {
            return TaskResult.Error("Task not found")
        }

        val updatedTask = task.copy(isCompleted = true)
        val updatedTasks = goal.tasks.map { if (it.id == taskId) updatedTask else it }
        
        // Recalculate goal progress
        val updatedGoal = goal.copy(
            tasks = updatedTasks,
            progress = calculateGoalProgress(updatedTasks)
        )
        
        goalRepository.updateGoal(updatedGoal)
        
        return TaskResult.Success(updatedTask)
    }

    override suspend fun markTaskAsIncomplete(goalId: String, taskId: String): TaskResult {
        delay(200)
        
        val goal = goalRepository.getGoal(goalId)
        if (goal == null) {
            return TaskResult.Error("Goal not found")
        }

        val task = goal.tasks.find { it.id == taskId }
        if (task == null) {
            return TaskResult.Error("Task not found")
        }

        val updatedTask = task.copy(isCompleted = false)
        val updatedTasks = goal.tasks.map { if (it.id == taskId) updatedTask else it }
        
        // Recalculate goal progress
        val updatedGoal = goal.copy(
            tasks = updatedTasks,
            progress = calculateGoalProgress(updatedTasks)
        )
        
        goalRepository.updateGoal(updatedGoal)
        
        return TaskResult.Success(updatedTask)
    }

    override fun calculateTaskProgress(task: Task): Int {
        if (task.allocatedHours <= 0) {
            return 0
        }
        val progress = (task.loggedHours / task.allocatedHours * 100).toInt()
        return progress.coerceIn(0, 100)
    }

    override fun validateHoursToLog(task: Task, hours: Float): Boolean {
        if (hours <= 0) {
            return false
        }
        val newTotal = task.loggedHours + hours
        return newTotal <= task.allocatedHours
    }

    /**
     * Calculate overall goal progress based on task completion
     */
    private fun calculateGoalProgress(tasks: List<Task>): Int {
        if (tasks.isEmpty()) {
            return 0
        }
        
        val totalAllocated = tasks.sumOf { it.allocatedHours.toDouble() }
        if (totalAllocated == 0.0) {
            // If no hours allocated, use task completion count
            val completedCount = tasks.count { it.isCompleted }
            return (completedCount * 100 / tasks.size).coerceIn(0, 100)
        }
        
        val totalLogged = tasks.sumOf { it.loggedHours.toDouble() }
        val progress = (totalLogged / totalAllocated * 100).toInt()
        return progress.coerceIn(0, 100)
    }
}


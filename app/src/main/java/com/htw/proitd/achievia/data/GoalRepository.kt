package com.htw.proitd.achievia.data

import com.htw.proitd.achievia.model.Goal
import com.htw.proitd.achievia.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object GoalRepository {
    private val _goals = MutableStateFlow<List<Goal>>(emptyList())
    val goals: StateFlow<List<Goal>> = _goals.asStateFlow()

    init {
        // Seed with some dummy data
        _goals.value = listOf(
            Goal(
                title = "Complete React Course",
                description = "Finish all modules and build final project",
                progress = 75,
                sharedWith = listOf("Sarah Johnson"),
                tasks = listOf(
                    Task(title = "Learn Hooks", description = "Complete useState, useEffect", allocatedTime = "10h allocated"),
                    Task(title = "Build Dashboard", description = "Create responsive admin dashboard", allocatedTime = "20h allocated")
                )
            ),
            Goal(
                title = "Morning Workout",
                description = "Exercise 5 days a week for 30 minutes",
                progress = 60
            ),
            Goal(
                title = "Read 12 Books This Year",
                description = "Read at least one book per month",
                progress = 42,
                sharedWith = listOf("Mike Chen", "Emily Davis")
            )
        )
    }

    fun addGoal(goal: Goal) {
        _goals.update { it + goal }
    }

    fun updateGoal(updatedGoal: Goal) {
        _goals.update { currentGoals ->
            currentGoals.map { if (it.id == updatedGoal.id) updatedGoal else it }
        }
    }

    fun deleteGoal(goalId: String) {
        _goals.update { currentGoals ->
            currentGoals.filter { it.id != goalId }
        }
    }
    
    fun getGoal(goalId: String): Goal? {
        return _goals.value.find { it.id == goalId }
    }
}

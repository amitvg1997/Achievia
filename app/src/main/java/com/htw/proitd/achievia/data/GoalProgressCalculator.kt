package com.htw.proitd.achievia.data

import com.htw.proitd.achievia.model.Task

/**
 * Converts logged hours to a percentage of goal completion.
 */
interface GoalProgressCalculator {
    /**
     * Returns a value between 0f and 100f representing total completion.
     * The total allocated hours across tasks is treated as 100%.
     */
    fun calculateGoalCompletion(tasks: List<Task>): Float
}

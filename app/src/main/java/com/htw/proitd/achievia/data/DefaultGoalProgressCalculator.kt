package com.htw.proitd.achievia.data

import com.htw.proitd.achievia.model.Task
import kotlin.math.roundToInt

/**
 * Computes completion by comparing logged hours against total allocated hours.
 */
class DefaultGoalProgressCalculator : GoalProgressCalculator {
    override fun calculateGoalCompletion(tasks: List<Task>): Float {
        val allocated = tasks.sumOf { it.allocatedHours.toDouble() }.toFloat()
        if (allocated <= 0f) return 0f

        val logged = tasks.sumOf { it.loggedHours.toDouble() }.toFloat()
        val percent = (logged / allocated) * 100f
        // Clamp to [0, 100] and reduce noise by rounding to whole percents.
        return percent.coerceIn(0f, 100f).roundToInt().toFloat()
    }
}

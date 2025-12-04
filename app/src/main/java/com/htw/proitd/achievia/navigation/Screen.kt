package com.htw.proitd.achievia.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Goals : Screen("goals")
    object CreateGoal : Screen("create_goal")
    object EditGoal : Screen("edit_goal/{goalId}") {
        fun createRoute(goalId: String) = "edit_goal/$goalId"
    }

    object GoalDetail : Screen("goal_detail/{goalId}") {
        fun createRoute(goalId: String) = "goal_detail/$goalId"
    }

    object Friends : Screen("friends")
    object Notifications : Screen("notifications")
    object Settings : Screen("settings")
}

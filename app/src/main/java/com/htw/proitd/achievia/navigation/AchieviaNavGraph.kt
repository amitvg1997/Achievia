package com.htw.proitd.achievia.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.htw.proitd.achievia.ui.screens.GoalDetailScreen
import com.htw.proitd.achievia.ui.screens.GoalsScreen
import com.htw.proitd.achievia.ui.screens.LoginScreen
import com.htw.proitd.achievia.ui.screens.RegisterScreen
import com.htw.proitd.achievia.ui.screens.GoalInputScreen

@Composable
fun AchieviaNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Screen.Goals.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                } },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(Screen.Goals.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                } },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable(Screen.Goals.route) {
            GoalsScreen(
                onNavigateToCreateGoal = { navController.navigate(Screen.CreateGoal.route) },
                onNavigateToEditGoal = { goalId -> navController.navigate(Screen.EditGoal.createRoute(goalId)) },
                onNavigateToGoalDetail = { goalId -> navController.navigate(Screen.GoalDetail.createRoute(goalId)) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) // Clear back stack
                    }
                }
            )
        }
        composable(Screen.CreateGoal.route) {
            GoalInputScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.EditGoal.route
        ) { backStackEntry ->
            val goalId = backStackEntry.arguments?.getString("goalId")
            GoalInputScreen(
                goalId = goalId,
                onNavigateBack = { navController.popBackStack() }
            )
        }


        composable(
            route = Screen.GoalDetail.route
        ) { backStackEntry ->
            val goalId = backStackEntry.arguments?.getString("goalId")
            GoalDetailScreen(
                goalId = goalId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

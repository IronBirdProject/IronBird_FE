package com.example.greetingcard.presentation.navigation

import TravelDestinationScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.greetingcard.presentation.view.plan.createplan.CalendarScreen
import com.example.greetingcard.presentation.view.plan.createplan.PlanTitleScreen

@Composable
fun PlanCreateNavGraph(
    rootNavController: NavController,
    startDestination: String = Screen.Calender.route
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Calender.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Calender.route)
            }
            CalendarScreen(
                navController = navController,
                rootNavController = rootNavController,
                planCreateViewModel = hiltViewModel(parentEntry)
            )
        }

        composable(Screen.SelectDestination.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Calender.route)
            }
            TravelDestinationScreen(
                navController = navController,
                planCreateViewModel = hiltViewModel(parentEntry)
            )
        }

        composable(Screen.PlanTitle.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Calender.route)
            }
            PlanTitleScreen(
                navController = navController,
                rootNavController = rootNavController,
                planCreateViewModel = hiltViewModel(parentEntry)
            )
        }

//        composable("done") { backStackEntry ->
//            val planCreateViewModel: PlanCreateViewModel = hiltViewModel(backStackEntry)
//            DoneScreen(rootNavController, planCreateViewModel)
//        }
    }
}

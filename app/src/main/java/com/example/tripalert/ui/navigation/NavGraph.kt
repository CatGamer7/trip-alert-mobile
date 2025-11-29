package com.example.tripalert.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tripalert.ui.screens.tripdetails.TripDetailsScreen
import com.example.tripalert.ui.screens.triplist.TripListScreen
import com.example.tripalert.ui.screens.user.UserScreen

@Composable
fun TripAlertNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = TripAlertDestinations.TRIP_LIST_ROUTE
    ) {
        // --- СПИСОК ПОЕЗДОК ---
        composable(TripAlertDestinations.TRIP_LIST_ROUTE) {
            TripListScreen(
                navController = navController,
                onAddClick = {
                    navController.navigate(TripAlertDestinations.tripDetailsRoute(-1L))
                },
                onTripClick = { id ->
                    navController.navigate(TripAlertDestinations.tripDetailsRoute(id))
                },
                onUserClick = {
                    // *** ИЗМЕНЕНИЕ: Навигация на статический маршрут без аргументов ***
                    navController.navigate(TripAlertDestinations.USER_ROUTE)
                }
            )
        }

        // --- ДЕТАЛИ ПОЕЗДКИ (Остается без изменений) ---
        composable(
            route = "${TripAlertDestinations.TRIP_DETAILS_ROUTE}/{${TripAlertDestinations.TRIP_DETAILS_ID_KEY}}",
            arguments = listOf(
                navArgument(TripAlertDestinations.TRIP_DETAILS_ID_KEY) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getLong(TripAlertDestinations.TRIP_DETAILS_ID_KEY) ?: -1L
            TripDetailsScreen(
                tripId = tripId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- ЭКРАН ПОЛЬЗОВАТЕЛЯ (Упрощено до статического маршрута) ---
        composable(TripAlertDestinations.USER_ROUTE) {
            // *** ИЗМЕНЕНИЕ: Убран ненужный аргумент usernameArg ***
            UserScreen(navController = navController)
        }

        // *** УДАЛЕНЫ СТАРЫЕ МАРШРУТЫ С {userId} и {username} ***
    }
}
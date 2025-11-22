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
                    // Навигация на экран пользователя
                    val currentUserId = 1L // здесь можно брать из репозитория текущего пользователя
                    navController.navigate("${TripAlertDestinations.USER_ROUTE}/$currentUserId")
                }
            )
        }

        // --- ДЕТАЛИ ПОЕЗДКИ ---
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

        // --- ЭКРАН ПОЛЬЗОВАТЕЛЯ ---
        composable(
            route = "${TripAlertDestinations.USER_ROUTE}/{userId}",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: 0L
            UserScreen(navController = navController, userId = userId)
        }
    }
}

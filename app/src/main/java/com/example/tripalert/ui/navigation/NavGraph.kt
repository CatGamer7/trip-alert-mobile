package com.example.tripalert.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tripalert.ui.screens.tripdetails.TripDetailsScreen
import com.example.tripalert.ui.screens.triplist.TripListScreen

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
                    // Переход на создание (id = -1 или null)
                    navController.navigate(TripAlertDestinations.tripDetailsRoute(-1L))
                },
                onTripClick = { id ->
                    // Переход на редактирование
                    navController.navigate(TripAlertDestinations.tripDetailsRoute(id))
                }
            )
        }

        // --- ДЕТАЛИ ПОЕЗДКИ ---
        composable(
            route = TripAlertDestinations.TRIP_DETAILS_ROUTE,
            arguments = listOf(
                navArgument(TripAlertDestinations.TRIP_DETAILS_ID_KEY) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            // Нам больше не нужно извлекать аргументы вручную здесь,
            // ViewModel сделает это сама через SavedStateHandle.

            TripDetailsScreen(
                // Передаем только навигационный колбэк
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
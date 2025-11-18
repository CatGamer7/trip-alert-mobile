package com.example.tripalert.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.koin.androidx.compose.koinViewModel
import com.example.tripalert.ui.screens.tripdetails.TripDetailsScreen
import com.example.tripalert.ui.screens.triplist.TripListScreen
import com.example.tripalert.ui.screens.tripdetails.TripDetailsViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun TripAlertNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "plans"
    ) {

        // --- СПИСОК ПОЕЗДОК ---
        composable("plans") {
            TripListScreen(
                navController = navController,
                onAddClick = {
                    navController.navigate("tripDetails")
                },
                onTripClick = { id ->
                    navController.navigate("tripDetails?tripId=$id")
                }
            )
        }

        // --- ДЕТАЛИ ПОЕЗДКИ ---
        composable(
            route = "tripDetails?tripId={tripId}",
            arguments = listOf(
                navArgument("tripId") {
                    type = NavType.LongType
                    defaultValue = -1L   // делаем необязательным через defaultValue
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("tripId") ?: -1L
            val tripId = if (id == -1L) null else id

            TripDetailsScreen(
                tripId = tripId,
                isEditing = tripId != null
            )
        }
    }
}
package com.example.tripalert.ui.navigation

object TripAlertDestinations {

    // --- Ключи аргументов ---

    // Ключ для передачи ID поездки. -1L означает "создать новую"
    const val TRIP_DETAILS_ID_KEY = "tripId"

    // --- Маршруты ---

    // Основной экран-заглушка (MainScreen)
    const val MAIN_ROUTE = "main"

    // Экран списка поездок
    const val TRIP_LIST_ROUTE = "tripList"

    // Экран деталей/создания (с аргументом)
    const val TRIP_DETAILS_ROUTE = "tripDetails/{$TRIP_DETAILS_ID_KEY}"

    // --- Вспомогательные функции для навигации ---

    /**
     * Построение маршрута для навигации на экран деталей.
     * @param tripId ID поездки для редактирования, или null/-1L для создания новой.
     */
    fun tripDetailsRoute(tripId: Long? = -1L): String {
        return "tripDetails/${tripId ?: -1L}"
    }
}
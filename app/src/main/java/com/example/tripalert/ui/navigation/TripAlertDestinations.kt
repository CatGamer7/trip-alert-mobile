object TripAlertDestinations {
    const val TRIP_LIST_ROUTE = "plans"
    const val TRIP_DETAILS_ROUTE = "tripDetails"
    const val TRIP_DETAILS_ID_KEY = "tripId"
    const val USER_ROUTE = "user"

    fun tripDetailsRoute(id: Long) = "$TRIP_DETAILS_ROUTE/$id"
    fun userRoute(userId: Long = 0L) = "$USER_ROUTE/$userId"
}

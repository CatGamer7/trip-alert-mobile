object TripAlertDestinations {
    const val TRIP_LIST_ROUTE = "trip_list"
    const val TRIP_DETAILS_ROUTE = "trip_details"
    const val USER_ROUTE = "user"

    const val TRIP_DETAILS_ID_KEY = "tripId"
    const val USERNAME_KEY = "username"

    fun tripDetailsRoute(id: Long) = "$TRIP_DETAILS_ROUTE/$id"
    fun userRoute(username: String) = "$USER_ROUTE/$username"
}

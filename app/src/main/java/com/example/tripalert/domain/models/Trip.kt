package com.example.tripalert.domain.models


import java.time.LocalDateTime


data class Trip(
    val id: Long = 0L,
    val userId: Long,
    val name: String,
    val origin: GeoPoint,
    val destination: GeoPoint,
    val plannedTime: LocalDateTime,
    val arrivalTime: LocalDateTime? = null,
    val transportType: TransportType = TransportType.WALK,
    val alertTime: LocalDateTime? = null,
    val originAddress: String? = null,
    val destinationAddress: String? = null
) {
    companion object {
        fun defaultTrip() = Trip(
            id = 0L,
            userId = 1L,
            name = "",
            origin = GeoPoint(0.0,0.0),
            destination = GeoPoint(0.0,0.0),
            plannedTime = LocalDateTime.now().plusHours(1),
            transportType = TransportType.WALK
        )
    }
}


data class GeoPoint(
    val latitude: Double,
    val longitude: Double
)
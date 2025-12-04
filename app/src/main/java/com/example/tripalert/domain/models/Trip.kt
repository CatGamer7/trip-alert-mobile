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
)

data class GeoPoint(
    val latitude: Double,
    val longitude: Double
)
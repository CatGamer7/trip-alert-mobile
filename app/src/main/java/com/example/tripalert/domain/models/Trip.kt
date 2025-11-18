package com.example.tripalert.domain.models

import java.time.LocalDateTime

data class Trip(
    val id: Long = 0,
    val userId: Long,
    val name: String,
    val origin: GeoPoint,
    val destination: GeoPoint,
    val plannedTime: LocalDateTime,
    val arrivalTime: LocalDateTime?,
    val transportType: TransportType,
    val alertTime: LocalDateTime?,

    val originAddress: String? = null,       // новое поле
    val destinationAddress: String? = null   // новое поле
)

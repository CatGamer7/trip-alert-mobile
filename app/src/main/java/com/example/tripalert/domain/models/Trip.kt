package com.example.tripalert.domain.models

import java.time.LocalDateTime

data class Trip(
    val id: Long = 0,
    val userId: Long,
    val name: String,

    val origin: GeoPoint,
    val destination: GeoPoint,

    // ✅ FIX: Поля для геокодирования (могут быть null, если координаты уже есть)
    val originAddress: String? = null,
    val destinationAddress: String? = null,

    val plannedTime: LocalDateTime,
    val arrivalTime: LocalDateTime? = null, // Может быть null
    val transportType: TransportType,
    val alertTime: LocalDateTime? = null // Может быть null
)

data class GeoPoint(
    val latitude: Double,
    val longitude: Double
)
// Enum TransportType и Resource<T> также должны быть определены
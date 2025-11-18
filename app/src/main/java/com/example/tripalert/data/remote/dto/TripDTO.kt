package com.example.tripalert.data.remote.dto

import java.time.LocalDateTime
import com.google.gson.annotations.SerializedName

// GeoJSON-backed coordinate DTO: represents a point, but structure mapped by GeoJsonPointAdapter
data class CoordinateDTO(
    val x: Double, // lon
    val y: Double  // lat
)

data class CreateTripDTO(
    val userId: Long,
    val name: String,
    val origin: CoordinateDTO,
    val destination: CoordinateDTO,
    val plannedTime: LocalDateTime,
    val arrivalTime: LocalDateTime? = null,
    val transportType: String,
    val alertTime: LocalDateTime? = null
)

data class UpdateTripDTO(
    val name: String? = null,
    val origin: CoordinateDTO? = null,
    val destination: CoordinateDTO? = null,
    val plannedTime: LocalDateTime? = null,
    val arrivalTime: LocalDateTime? = null,
    val transportType: String? = null,
    val alertTime: LocalDateTime? = null
)

data class TripResponseDTO(
    val id: Long,
    val userId: Long,
    val name: String,
    val origin: CoordinateDTO,
    val destination: CoordinateDTO,
    val plannedTime: LocalDateTime,
    val arrivalTime: LocalDateTime?,
    val transportType: String,
    val alertTime: LocalDateTime?
)
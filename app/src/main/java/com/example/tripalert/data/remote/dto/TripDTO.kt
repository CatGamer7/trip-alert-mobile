package com.example.tripalert.data.remote.dto

import com.example.tripalert.util.GeoJsonPointAdapter
import com.example.tripalert.domain.models.GeoPoint
import com.example.tripalert.domain.models.TransportType
import com.google.gson.annotations.JsonAdapter
import java.time.LocalDateTime

data class CoordinateDTO(
    val x: Double,
    val y: Double
)

data class CreateTripDTO(
    val userId: Long,
    val name: String,

    @JsonAdapter(GeoJsonPointAdapter::class)
    val origin: GeoPoint,
    @JsonAdapter(GeoJsonPointAdapter::class)
    val destination: GeoPoint,
    val plannedTime: LocalDateTime,
    val arrivalTime: LocalDateTime?,
    val transportType: TransportType = TransportType.WALK,
    val reminderData: CreateReminderDTO,
    )

data class UpdateTripDTO(
    val name: String? = null,
    @JsonAdapter(GeoJsonPointAdapter::class)
    val origin: GeoPoint? = null,
    @JsonAdapter(GeoJsonPointAdapter::class)
    val destination: GeoPoint? = null,
    val plannedTime: LocalDateTime? = null,
    val arrivalTime: LocalDateTime? = null,
    val transportType: TransportType? = null,
    val reminderData: UpdateReminderDTO? = null
)

data class TripResponseDTO(
    val id: Long,
    val userId: Long,
    val name: String,
    @JsonAdapter(GeoJsonPointAdapter::class) val origin: GeoPoint,
    @JsonAdapter(GeoJsonPointAdapter::class) val destination: GeoPoint,
    val plannedTime: LocalDateTime,
    val arrivalTime: LocalDateTime?,
    val transportType: TransportType,
    val reminderData: CreateReminderDTO
)

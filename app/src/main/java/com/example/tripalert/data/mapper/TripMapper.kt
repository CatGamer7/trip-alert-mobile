package com.example.tripalert.data.mapper

import com.example.tripalert.data.remote.dto.CoordinateDTO
import com.example.tripalert.data.remote.dto.CreateTripDTO
import com.example.tripalert.data.remote.dto.TripResponseDTO
import com.example.tripalert.data.remote.dto.UpdateTripDTO
import com.example.tripalert.domain.models.GeoPoint
import com.example.tripalert.domain.models.TransportType
import com.example.tripalert.domain.models.Trip

object TripMapper {

    fun fromDto(dto: TripResponseDTO): Trip = Trip(
        id = dto.id,
        userId = dto.userId,
        name = dto.name,
        origin = fromCoordinateDTO(dto.origin),
        destination = fromCoordinateDTO(dto.destination),
        plannedTime = dto.plannedTime,
        arrivalTime = dto.arrivalTime,
        transportType = fromTransportTypeString(dto.transportType),
        alertTime = dto.alertTime
    )

    fun toCreateDto(trip: Trip): CreateTripDTO = CreateTripDTO(
        userId = trip.userId,
        name = trip.name,
        origin = toCoordinateDTO(trip.origin),
        destination = toCoordinateDTO(trip.destination),
        plannedTime = trip.plannedTime,
        arrivalTime = trip.arrivalTime,
        transportType = trip.transportType.name.lowercase(),
        alertTime = trip.alertTime
    )

    fun toUpdateDto(trip: Trip): UpdateTripDTO = UpdateTripDTO(
        name = trip.name,
        origin = toCoordinateDTO(trip.origin),
        destination = toCoordinateDTO(trip.destination),
        plannedTime = trip.plannedTime,
        arrivalTime = trip.arrivalTime,
        transportType = trip.transportType.name.lowercase(),
        alertTime = trip.alertTime
    )

    private fun toCoordinateDTO(point: GeoPoint) = CoordinateDTO(x = point.longitude, y = point.latitude)
    private fun fromCoordinateDTO(dto: CoordinateDTO) = GeoPoint(latitude = dto.y, longitude = dto.x)

    private fun fromTransportTypeString(type: String?): TransportType =
        try {
            if (type == null) TransportType.WALK else TransportType.valueOf(type.uppercase())
        } catch (e: IllegalArgumentException) {
            TransportType.WALK
        }
}

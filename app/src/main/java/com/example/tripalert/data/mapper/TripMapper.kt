package com.example.tripalert.data.mapper

import com.example.tripalert.data.local.entity.TripEntity
import com.example.tripalert.data.remote.dto.*
import com.example.tripalert.domain.models.GeoPoint
import com.example.tripalert.domain.models.TransportType
import com.example.tripalert.domain.models.Trip

object TripMapper {

    fun mapEntityToDomain(entity: TripEntity): Trip = Trip(
        id = entity.id,
        userId = entity.userId,
        name = entity.name,
        origin = GeoPoint(entity.originLat, entity.originLon),
        destination = GeoPoint(entity.destinationLat, entity.destinationLon),
        plannedTime = entity.plannedTime,
        arrivalTime = entity.arrivalTime,
        transportType = TransportType.valueOf(entity.transportType),
        alertTime = entity.alertTime,
        originAddress = null,
        destinationAddress = null
    )

    fun mapDomainToEntity(domain: Trip): TripEntity = TripEntity(
        id = domain.id,
        userId = domain.userId,
        name = domain.name,
        plannedTime = domain.plannedTime,
        arrivalTime = domain.arrivalTime,
        transportType = domain.transportType.name,
        alertTime = domain.alertTime,
        originLat = domain.origin.latitude,
        originLon = domain.origin.longitude,
        destinationLat = domain.destination.latitude,
        destinationLon = domain.destination.longitude
    )

    fun mapDtoToEntity(dto: TripResponseDTO): TripEntity = TripEntity(
        id = dto.id,
        userId = dto.userId,
        name = dto.name,
        plannedTime = dto.plannedTime,
        arrivalTime = dto.arrivalTime,
        transportType = dto.transportType.name,
        alertTime = dto.reminderData.notificationTime,
        originLat = dto.origin.latitude,
        originLon = dto.origin.longitude,
        destinationLat = dto.destination.latitude,
        destinationLon = dto.destination.longitude
    )

    fun mapDtoToDomain(dto: TripResponseDTO): Trip = Trip(
        id = dto.id,
        userId = dto.userId,
        name = dto.name,
        origin = dto.origin,
        destination = dto.destination,
        plannedTime = dto.plannedTime,
        arrivalTime = dto.arrivalTime,
        transportType = dto.transportType,
        alertTime = dto.reminderData.notificationTime,
        originAddress = null,
        destinationAddress = null
    )

    fun mapDomainToCreateDto(domain: Trip): CreateTripDTO = CreateTripDTO(
        userId = domain.userId,
        name = domain.name,
        origin = domain.origin,
        destination = domain.destination,
        plannedTime = domain.plannedTime,
        arrivalTime = domain.arrivalTime
            ?: throw IllegalArgumentException("Arrival time must be set for creation."),
        transportType = domain.transportType,
        reminderData = CreateReminderDTO(
            notificationTime = domain.alertTime
                ?: throw IllegalArgumentException("Alert time required for creation.")
        )
    )

    fun mapDomainToUpdateDto(domain: Trip): UpdateTripDTO = UpdateTripDTO(
        name = domain.name,
        origin = domain.origin,
        destination = domain.destination,
        plannedTime = domain.plannedTime,
        arrivalTime = domain.arrivalTime,
        transportType = domain.transportType,
        reminderData = domain.alertTime?.let { UpdateReminderDTO(notificationTime = it) }
    )
}

package com.example.tripalert.data.mapper

import com.example.tripalert.data.local.entity.TripEntity
import com.example.tripalert.data.remote.dto.*
import com.example.tripalert.domain.models.GeoPoint
import com.example.tripalert.domain.models.TransportType
import com.example.tripalert.domain.models.Trip
import java.time.LocalDateTime

object TripMapper {

    // --- DTO <-> Domain (Чтение из сети) ---

    fun mapDtoToDomain(dto: TripResponseDTO): Trip {
        return Trip(
            id = dto.id,
            userId = dto.userId,
            name = dto.name,
            // (y, x) -> (Lat, Lon)
            origin = GeoPoint(dto.origin.y, dto.origin.x),
            destination = GeoPoint(dto.destination.y, dto.destination.x),
            plannedTime = dto.plannedTime,
            arrivalTime = dto.arrivalTime,
            transportType = TransportType.valueOf(dto.transportType),
            alertTime = dto.alertTime,
            originAddress = "",
            destinationAddress = ""
        )
    }

    // --- Domain -> CreateTripDTO (Создание) ---

    fun mapDomainToCreateDto(domain: Trip): CreateTripDTO {
        return CreateTripDTO(
            userId = domain.userId,
            name = domain.name,
            // (Lat, Lon) -> (Lon, Lat) -> (x, y)
            origin = CoordinateDTO(domain.origin.longitude, domain.origin.latitude),
            destination = CoordinateDTO(domain.destination.longitude, domain.destination.latitude),
            plannedTime = domain.plannedTime,
            arrivalTime = domain.arrivalTime,
            transportType = domain.transportType.name,
            alertTime = domain.alertTime
        )
    }

    // --- Domain -> UpdateTripDTO (Обновление) ---

    fun mapDomainToUpdateDto(domain: Trip): UpdateTripDTO {
        return UpdateTripDTO(
            name = domain.name,
            origin = CoordinateDTO(domain.origin.longitude, domain.origin.latitude),
            destination = CoordinateDTO(domain.destination.longitude, domain.destination.latitude),
            plannedTime = domain.plannedTime,
            arrivalTime = domain.arrivalTime,
            transportType = domain.transportType.name,
            alertTime = domain.alertTime
        )
    }

    // --- Entity <-> Domain (для локальной БД) ---

    fun mapEntityToDomain(entity: TripEntity): Trip {
        return Trip(
            id = entity.id,
            userId = entity.userId,
            name = entity.name,
            origin = GeoPoint(entity.originLat, entity.originLon),
            destination = GeoPoint(entity.destinationLat, entity.destinationLon),
            plannedTime = entity.plannedTime,
            arrivalTime = entity.arrivalTime,
            transportType = TransportType.valueOf(entity.transportType),
            alertTime = entity.alertTime,
            originAddress = "", // Предполагается, что адрес хранится где-то еще или должен быть загружен
            destinationAddress = ""
        )
    }

    fun mapDomainToEntity(domain: Trip): TripEntity {
        return TripEntity(
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
    }

    // --- DTO -> Entity (для кэширования) ---

    fun mapDtoToEntity(dto: TripResponseDTO): TripEntity {
        return TripEntity(
            id = dto.id,
            userId = dto.userId,
            name = dto.name,
            plannedTime = dto.plannedTime,
            arrivalTime = dto.arrivalTime,
            transportType = dto.transportType,
            alertTime = dto.alertTime,
            originLat = dto.origin.y,
            originLon = dto.origin.x,
            destinationLat = dto.destination.y,
            destinationLon = dto.destination.x
        )
    }
}
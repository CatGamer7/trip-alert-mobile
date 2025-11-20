package com.example.tripalert.data.mapper

import com.example.tripalert.data.local.entity.TripEntity
import com.example.tripalert.data.remote.dto.*
import com.example.tripalert.domain.models.GeoPoint
import com.example.tripalert.domain.models.TransportType
import com.example.tripalert.domain.models.Trip
import java.time.LocalDateTime

object TripMapper {

    // --- Entity -> Domain (Чтение из БД) ---
    // ✅ FIX: Убрана ошибочная заглушка LocalDateTime.MIN
    fun mapEntityToDomain(entity: TripEntity): Trip {
        return Trip(
            id = entity.id,
            userId = entity.userId,
            name = entity.name,
            origin = GeoPoint(entity.originLat, entity.originLon),
            destination = GeoPoint(entity.destinationLat, entity.destinationLon),
            plannedTime = entity.plannedTime,
            arrivalTime = entity.arrivalTime, // Null-Safety OK
            transportType = TransportType.valueOf(entity.transportType),
            alertTime = entity.alertTime, // Null-Safety OK
            originAddress = null, // Не хранится в Entity
            destinationAddress = null
        )
    }

    // --- Domain -> Entity (Запись в БД) ---

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

    // --- DTO -> Entity (Кэширование из сети в БД) ---
    // ✅ FIX: Извлечение Lat/Lon из GeoPoint (предполагая DTO исправлены)
    fun mapDtoToEntity(dto: TripResponseDTO): TripEntity {
        return TripEntity(
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
    }

    // --- DTO -> Domain (Чтение ответа сервера) ---
    fun mapDtoToDomain(dto: TripResponseDTO): Trip {
        return Trip(
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
    }

    // --- ✅ ДОБАВЛЕНО: Domain -> DTO (Создание запроса) ---
    fun mapDomainToCreateDto(domain: Trip): CreateTripDTO {
        return CreateTripDTO(
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
    }

    // --- ✅ ДОБАВЛЕНО: Domain -> DTO (Обновление запроса) ---
    fun mapDomainToUpdateDto(domain: Trip): UpdateTripDTO {
        return UpdateTripDTO(
            name = domain.name,
            origin = domain.origin,
            destination = domain.destination,
            plannedTime = domain.plannedTime,
            arrivalTime = domain.arrivalTime,
            transportType = domain.transportType,
            // Создаем DTO напоминания, только если alertTime не null
            reminderData = domain.alertTime?.let {
                UpdateReminderDTO(notificationTime = it)
            }
        )
    }
}
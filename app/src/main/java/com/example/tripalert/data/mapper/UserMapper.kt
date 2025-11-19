package com.example.tripalert.data.mapper

import com.example.tripalert.data.remote.dto.CreateUserDTO
import com.example.tripalert.data.remote.dto.UpdateUserDTO
import com.example.tripalert.data.remote.dto.UserResponseDTO
import com.example.tripalert.domain.models.TransportType
import com.example.tripalert.domain.models.User

object UserMapper {

    // ⚠️ ВАЖНО: Уточни у бэкендера, какие цифры соответствуют какому типу!
    // Пока предполагаем: 0-Walk, 1-Car, 2-Bus, 3-Taxi, 4-Scooter
    private fun transportToId(type: TransportType): Int = when (type) {
        TransportType.WALK -> 0
        TransportType.CAR -> 1
        TransportType.BUS -> 2
        TransportType.TAXI -> 3
        TransportType.SCOOTER -> 4
    }

    private fun idToTransport(id: Int): TransportType = when (id) {
        0 -> TransportType.WALK
        1 -> TransportType.CAR
        2 -> TransportType.BUS
        3 -> TransportType.TAXI
        4 -> TransportType.SCOOTER
        else -> TransportType.WALK // Дефолтное значение, если пришел неизвестный ID
    }

    fun fromDto(dto: UserResponseDTO, password: String = ""): User = User(
        id = dto.id,
        username = dto.username,
        // Если сервер не прислал email, генерируем заглушку
        email = dto.email ?: "${dto.username}@tripalert.com",
        password = password,
        timeOffset = dto.timeOffset,
        // Конвертируем Int из БД в Enum
        preferredTransport = idToTransport(dto.preferredTransport)
    )

    fun toCreateDto(user: User): CreateUserDTO = CreateUserDTO(
        username = user.username,
        email = user.email,
        password = user.password,
        timeOffset = user.timeOffset,
        // Конвертируем Enum в Int для отправки
        preferredTransport = transportToId(user.preferredTransport)
    )

    fun toUpdateDto(user: User): UpdateUserDTO = UpdateUserDTO(
        timeOffset = user.timeOffset,
        preferredTransport = transportToId(user.preferredTransport)
    )
}
package com.example.tripalert.data.mapper

import com.example.tripalert.data.remote.dto.CreateUserDTO
import com.example.tripalert.data.remote.dto.UpdateUserDTO
import com.example.tripalert.data.remote.dto.UserResponseDTO
import com.example.tripalert.domain.models.User
import com.example.tripalert.domain.models.TransportType

object UserMapper {

    fun fromDto(dto: UserResponseDTO, password: String = ""): User = User(
        id = dto.id,
        username = dto.username,
        // В DTO больше нет email, генерируем заглушку или оставляем пустым,
        // в зависимости от логики Domain модели.
        email = "${dto.username}@tripalert.com",
        password = password,
        timeOffset = dto.timeOffset,
        // DTO теперь возвращает Enum, ручная конвертация не нужна
        preferredTransport = dto.preferredTransport
    )

    fun toCreateDto(user: User): CreateUserDTO = CreateUserDTO(
        username = user.username,
        password = user.password,
        timeOffset = user.timeOffset,
        preferredTransport = user.preferredTransport // Передаем Enum напрямую
    )

    fun toUpdateDto(user: User): UpdateUserDTO = UpdateUserDTO(
        timeOffset = user.timeOffset,
        preferredTransport = user.preferredTransport
    )
}
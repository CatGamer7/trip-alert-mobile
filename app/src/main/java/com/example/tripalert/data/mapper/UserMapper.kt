package com.example.tripalert.data.mapper

import com.example.tripalert.data.remote.dto.CreateUserDTO
import com.example.tripalert.data.remote.dto.UpdateUserDTO
import com.example.tripalert.data.remote.dto.UserResponseDTO
import com.example.tripalert.domain.models.TransportType
import com.example.tripalert.domain.models.User

object UserMapper {

    fun fromDto(dto: UserResponseDTO, password: String = ""): User = User(
        id = dto.id,
        username = dto.username,
        email = dto.username + "@tripalert.com", // если сервер не возвращает email
        password = password,
        timeOffset = dto.timeOffset,
        preferredTransport = TransportType.valueOf(dto.preferredTransport.uppercase())
    )

    fun toCreateDto(user: User): CreateUserDTO = CreateUserDTO(
        username = user.username,
        email = user.username,
        password = user.password,
        timeOffset = user.timeOffset,
        preferredTransport = user.preferredTransport.name.lowercase()
    )

    fun toUpdateDto(user: User): UpdateUserDTO = UpdateUserDTO(
        timeOffset = user.timeOffset,
        preferredTransport = user.preferredTransport.name.lowercase()
    )
}

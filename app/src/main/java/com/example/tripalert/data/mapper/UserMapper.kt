package com.example.tripalert.data.mapper

import com.example.tripalert.data.remote.dto.CreateUserDTO
import com.example.tripalert.data.remote.dto.UpdateUserDTO
import com.example.tripalert.data.remote.dto.UserResponseDTO
import com.example.tripalert.domain.models.User

object UserMapper {


    fun fromDto(dto: UserResponseDTO, password: String = ""): User {
        return User(
            username = dto.username,
            timeOffset = dto.timeOffset,
            preferredTransport = dto.preferredTransport
        )
    }

    fun toCreateDto(user: User, password: String): CreateUserDTO =
        CreateUserDTO(
            username = user.username,
            password = password,
            timeOffset = user.timeOffset,
            preferredTransport = user.preferredTransport
        )

    fun toUpdateDto(user: User): UpdateUserDTO =
        UpdateUserDTO(
            timeOffset = user.timeOffset,
            preferredTransport = user.preferredTransport,
            username = user.username
        )
}

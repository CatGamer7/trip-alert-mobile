package com.example.tripalert.data.remote.dto

import com.example.tripalert.domain.models.TransportType
import com.google.gson.annotations.SerializedName

data class CreateUserDTO(
    val username: String,
    val password: String,
    val timeOffset: Int = 10,
    val preferredTransport: TransportType
)

data class LoginRequestDTO(
    val username: String,
    val password: String
)

data class UpdateUserDTO(
    val username: String,
    val timeOffset: Int? = null,
    val preferredTransport: TransportType? = null,
)

data class UserResponseDTO(
    val id: Long,
    val username: String,
    val timeOffset: Int,
    val preferredTransport: TransportType
)

data class AuthResponseDTO(
    val token: String,
    val user: UserResponseDTO,
    val tokenType: String? = null
)

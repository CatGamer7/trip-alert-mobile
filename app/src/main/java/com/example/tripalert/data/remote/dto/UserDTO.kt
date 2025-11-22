package com.example.tripalert.data.remote.dto

import com.example.tripalert.domain.models.TransportType
import com.google.gson.annotations.SerializedName

data class CreateUserDTO(
    val username: String,
    val password: String,
    val timeOffset: Int = 10,
    val preferredTransport: TransportType
)

data class UpdateUserDTO(
    val timeOffset: Int? = null,
    val preferredTransport: TransportType? = null
)

data class UserResponseDTO(
    val username: String,
    val timeOffset: Int,
    val preferredTransport: TransportType
)

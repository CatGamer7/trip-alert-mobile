package com.example.tripalert.data.remote.dto

data class CreateUserDTO(
    val username: String,
    val password: String,
    val email: String,
    val timeOffset: Int = 15,
    val preferredTransport: String = "WALK"
)

data class UpdateUserDTO(
    val username: String? = null,
    val password: String? = null,
    val email: String? = null,
    val timeOffset: Int? = null,
    val preferredTransport: String? = null
)

data class UserResponseDTO(
    val id: Long,
    val username: String,
    val email: String,
    val password: String,
    val timeOffset: Int,
    val preferredTransport: String
)

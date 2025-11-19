package com.example.tripalert.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateUserDTO(
    val username: String,
    val password: String,
    val email: String,
    val timeOffset: Int = 15,
    // ИСПРАВЛЕНО: Int согласно схеме БД (smallint)
    val preferredTransport: Int
)

data class UpdateUserDTO(
    val username: String? = null,
    val password: String? = null,
    val email: String? = null,
    val timeOffset: Int? = null,
    // ИСПРАВЛЕНО: Int согласно схеме БД (smallint)
    val preferredTransport: Int? = null
)

data class UserResponseDTO(
    val id: Long,
    val username: String,
    val email: String?, // Может быть null, если бэк не отдает
    val timeOffset: Int,
    // ИСПРАВЛЕНО: Int согласно схеме БД (smallint)
    val preferredTransport: Int
)
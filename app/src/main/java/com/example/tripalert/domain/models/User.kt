package com.example.tripalert.domain.models

import com.example.tripalert.domain.models.TransportType

data class User(
    val id: Long,
    val username: String,
    val password: String,
    val timeOffset: Int = 15,
    val preferredTransport: TransportType
)

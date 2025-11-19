package com.example.tripalert.domain.models

data class User(
    val id: Long,
    val username: String,
    val email: String,
    val password: String,
    val timeOffset: Int = 15,
    val preferredTransport: TransportType
)
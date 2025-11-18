package com.example.tripalert.domain.models

data class User(
    val id: Long,
    val username: String,
    val email: String,
    val password: String, // хэш хранится на сервере
    val timeOffset: Int = 15, //setting
    val preferredTransport: TransportType //setting
)
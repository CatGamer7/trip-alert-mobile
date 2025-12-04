package com.example.tripalert.domain.models

data class User(
    val username: String,
    val timeOffset: Int,
    val preferredTransport: TransportType
)
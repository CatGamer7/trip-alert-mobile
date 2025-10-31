package com.example.tripalert.models

data class Settings(
    val id: Int,
    val user: User,
    val timeOffset: Int,
    val preferredTransport: TransportationType
)

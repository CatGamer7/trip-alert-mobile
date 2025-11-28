package com.example.tripalert.domain.models

import com.example.tripalert.domain.models.TransportType

data class User(
    val username: String,
    val timeOffset: Int,
    val preferredTransport: TransportType
)
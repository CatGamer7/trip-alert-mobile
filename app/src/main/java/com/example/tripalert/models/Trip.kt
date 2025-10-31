package com.example.tripalert.models

import java.time.LocalDateTime

data class Trip(
    val id: Int,
    val user: User,
    val name: String,
    val origin: String,
    val destination: String,
    val plannedTime: LocalDateTime,
    val arrivalTime: LocalDateTime,
    val transportation: TransportationType,
    val alertTime: Int = 15
)

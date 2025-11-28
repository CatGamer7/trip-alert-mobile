package com.example.tripalert.data.remote.dto

import java.time.LocalDateTime

data class CreateReminderDTO(
    val notificationTime: LocalDateTime
)

data class UpdateReminderDTO(
    val notificationTime: LocalDateTime? = null,
    val sent: Boolean? = null
)

data class ReminderResponseDTO(
    val id: Long,
    val tripId: Long,
    val notificationTime: LocalDateTime,
    val sent: Boolean
)
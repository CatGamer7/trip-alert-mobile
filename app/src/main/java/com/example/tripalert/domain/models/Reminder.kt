package com.example.tripalert.domain.models

import java.time.LocalDateTime

data class Reminder(
    val id: Long,
    val trip: Trip,
    val notificationTime: LocalDateTime,
    val sent: Boolean
)
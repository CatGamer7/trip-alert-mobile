package com.example.tripalert.models

import java.time.LocalDateTime

data class Reminders(
    val id: Int,
    val trip: Trip,
    val notificationTime: LocalDateTime,
    val sent: Boolean
)

package com.example.tripalert.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

// Название таблицы соответствует вашей схеме БД
@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Поля, которые вызвали ошибку "No parameter..."
    val userId: Long,
    val name: String,
    val plannedTime: LocalDateTime,
    val arrivalTime: LocalDateTime?,
    val transportType: String,
    val alertTime: LocalDateTime?, // Из схемы reminders

    // Координаты (вместо 'geometry')
    val originLat: Double,
    val originLon: Double,
    val destinationLat: Double,
    val destinationLon: Double
)
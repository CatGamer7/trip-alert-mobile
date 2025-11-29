package com.example.tripalert.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val userId: Long,
    val name: String,
    val plannedTime: LocalDateTime,
    val arrivalTime: LocalDateTime?,
    val transportType: String,
    val alertTime: LocalDateTime?,

    val originLat: Double,
    val originLon: Double,
    val destinationLat: Double,
    val destinationLon: Double
)
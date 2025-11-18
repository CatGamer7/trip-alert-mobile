package com.example.tripalert.domain.repository

import com.example.tripalert.domain.models.Trip

interface TripRepository {
    suspend fun getTrips(): List<Trip>
    suspend fun getTripById(id: Long): Trip
    suspend fun createTrip(trip: Trip): Trip
    suspend fun updateTrip(trip: Trip): Trip
    suspend fun deleteTrip(id: Long)
}
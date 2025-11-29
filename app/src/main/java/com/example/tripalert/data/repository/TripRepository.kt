package com.example.tripalert.domain.repository

import com.example.tripalert.domain.models.Trip
import com.example.tripalert.util.Resource

interface TripRepository {
    suspend fun getAllTrips(): Resource<List<Trip>>
    suspend fun getTripById(id: Long): Resource<Trip>
    suspend fun createTrip(trip: Trip): Resource<Unit>
    suspend fun updateTrip(trip: Trip): Resource<Unit>
    suspend fun deleteTrip(id: Long): Resource<Unit>
}

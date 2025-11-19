package com.example.tripalert.domain.repository

import com.example.tripalert.domain.models.Trip
import com.example.tripalert.util.Resource // Импорт Resource
import kotlinx.coroutines.flow.Flow

interface TripRepository {

    // 1. ИСПРАВЛЕНО: Теперь возвращает Flow<Resource<List<Trip>>> и принимает userId
    fun getTrips(userId: Long): Flow<Resource<List<Trip>>>

    // 2. ИСПРАВЛЕНО: Теперь возвращает Resource<Trip>
    suspend fun getTripById(tripId: Long): Resource<Trip>

    // 3. ИСПРАВЛЕНО: Теперь возвращает Resource<Unit>
    suspend fun createTrip(trip: Trip): Resource<Trip>

    // 4. ИСПРАВЛЕНО: Теперь возвращает Resource<Unit>
    suspend fun updateTrip(trip: Trip): Resource<Unit>

    // 5. ИСПРАВЛЕНО: Теперь возвращает Resource<Unit>
    suspend fun deleteTrip(tripId: Long): Resource<Unit>
}
package com.example.tripalert.domain.usecase.trip

import com.example.tripalert.domain.models.Trip
import com.example.tripalert.domain.repository.TripRepository

class GetTripsUseCase(private val repository: TripRepository) {
    suspend operator fun invoke(): List<Trip> {
        return repository.getTrips().sortedBy { it.plannedTime }
    }
}
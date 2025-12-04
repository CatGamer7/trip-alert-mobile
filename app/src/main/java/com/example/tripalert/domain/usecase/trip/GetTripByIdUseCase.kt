package com.example.tripalert.domain.usecase.trip

import com.example.tripalert.domain.models.Trip
import com.example.tripalert.domain.repository.TripRepository
import com.example.tripalert.util.Resource

class GetTripByIdUseCase(private val repository: TripRepository) {
    suspend operator fun invoke(id: Long): Resource<Trip> {
        return repository.getTripById(id)
    }
}
package com.example.tripalert.domain.usecase.trip

import com.example.tripalert.domain.repository.TripRepository

class DeleteTripUseCase(private val repository: TripRepository) {
    suspend operator fun invoke(id: Long) {
        require(id > 0) { "Невалидный ID поездки" }
        repository.deleteTrip(id)
    }
}
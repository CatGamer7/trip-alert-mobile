package com.example.tripalert.domain.usecase.trip

import com.example.tripalert.domain.models.Trip
import com.example.tripalert.domain.repository.TripRepository
import com.example.tripalert.util.Resource

class UpdateTripUseCase(private val repository: TripRepository) {

    // Возвращает Resource<Unit>, так как обновление не возвращает объект
    suspend operator fun invoke(trip: Trip): Resource<Unit> {
        if (trip.id <= 0) {
            return Resource.Error("Некорректный ID поездки")
        }
        return repository.updateTrip(trip)
    }
}
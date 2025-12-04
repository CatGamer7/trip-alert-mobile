package com.example.tripalert.domain.usecase.trip

import com.example.tripalert.domain.models.Trip
import com.example.tripalert.domain.repository.TripRepository
import com.example.tripalert.util.Resource

class CreateTripUseCase(private val repository: TripRepository) {

    suspend operator fun invoke(trip: Trip): Resource<out Any> {
        if (trip.originAddress.isNullOrBlank()) {
            return Resource.Error("Адрес отправления не указан")
        }

        val inputTrip = trip.copy(arrivalTime = null, alertTime = null)
        return repository.createTrip(inputTrip)
    }
}
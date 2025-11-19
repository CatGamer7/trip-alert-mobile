package com.example.tripalert.domain.usecase.trip

import com.example.tripalert.domain.models.Trip
import com.example.tripalert.domain.repository.TripRepository
import com.example.tripalert.util.Resource
import java.time.LocalDateTime

class CreateTripUseCase(private val repository: TripRepository) {

    // Теперь возвращает Resource<Trip>, а не просто Trip
    suspend operator fun invoke(trip: Trip): Resource<Trip> {
        if (trip.originAddress.isNullOrBlank()) {
            return Resource.Error("Адрес отправления не указан")
        }

        // Сброс серверных полей (если нужно)
        val inputTrip = trip.copy(arrivalTime = null, alertTime = null)
        return repository.createTrip(inputTrip)
    }
}
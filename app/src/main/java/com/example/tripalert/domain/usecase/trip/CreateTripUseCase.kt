package com.example.tripalert.domain.usecase.trip

import com.example.tripalert.domain.models.Trip
import com.example.tripalert.domain.repository.TripRepository
import java.time.LocalDateTime

class CreateTripUseCase(private val repository: TripRepository) {
    suspend operator fun invoke(trip: Trip): Trip {
        require(trip.name.isNotBlank()) { "Имя поездки не может быть пустым." }
        require(trip.plannedTime.isAfter(LocalDateTime.now())) { "Планируемое время должно быть в будущем." }

        // Сброс серверных полей
        val inputTrip = trip.copy(arrivalTime = null, alertTime = null)
        return repository.createTrip(inputTrip)
    }
}
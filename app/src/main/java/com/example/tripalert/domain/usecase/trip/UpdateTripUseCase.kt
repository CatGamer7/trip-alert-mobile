package com.example.tripalert.domain.usecase.trip

import com.example.tripalert.domain.models.Trip
import com.example.tripalert.domain.repository.TripRepository
import java.time.LocalDateTime

class UpdateTripUseCase(private val repository: TripRepository) {
    suspend operator fun invoke(trip: Trip): Trip {
        require(trip.id > 0) { "ID поездки должен быть > 0" }
        require(trip.name.isNotBlank()) { "Имя поездки не может быть пустым." }
        require(trip.plannedTime.isAfter(LocalDateTime.now())) { "Планируемое время должно быть в будущем." }

        val inputTrip = trip.copy(arrivalTime = null, alertTime = null)
        return repository.updateTrip(inputTrip)
    }
}
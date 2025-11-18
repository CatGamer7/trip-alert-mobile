package com.example.tripalert.domain.usecase.trip

import com.example.tripalert.domain.models.Trip
import com.example.tripalert.domain.repository.TripRepository

class GetTripByIdUseCase(private val repository: TripRepository) {
    suspend operator fun invoke(id: Long): Trip {
        try {
            return repository.getTripById(id)
        } catch (e: Exception) {
            throw TripNotFoundException("Поездка с ID $id не найдена", e)
        }
    }
}// Собственное исключение для доменного слоя
class TripNotFoundException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
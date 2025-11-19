package com.example.tripalert.domain.usecase.trip

import com.example.tripalert.domain.models.Trip
import com.example.tripalert.domain.repository.TripRepository
import com.example.tripalert.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTripsUseCase(private val repository: TripRepository) {

    // Принимает userId и возвращает Flow<Resource...>
    operator fun invoke(userId: Long): Flow<Resource<List<Trip>>> {
        return repository.getTrips(userId).map { resource ->
            // Если успех, сортируем список по времени
            if (resource is Resource.Success) {
                val sortedList = resource.data?.sortedBy { it.plannedTime } ?: emptyList()
                Resource.Success(sortedList)
            } else {
                resource
            }
        }
    }
}
package com.example.tripalert.domain.usecase.trip

import com.example.tripalert.domain.models.Trip
import com.example.tripalert.domain.repository.TripRepository
import com.example.tripalert.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTripsUseCase(private val repository: TripRepository) {
    operator fun invoke(): Flow<Resource<List<Trip>>> = flow {
        emit(Resource.Loading())
        try {
            when (val result = repository.getAllTrips()) {
                is Resource.Success ->
                    emit(Resource.Success(result.data ?: emptyList()))

                is Resource.Error ->
                    emit(Resource.Error(result.message ?: "Ошибка загрузки"))

                is Resource.Loading ->
                    emit(Resource.Loading())
                else -> {}
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Неизвестная ошибка"))
        }
    }
}

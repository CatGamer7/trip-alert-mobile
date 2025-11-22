package com.example.tripalert.data.repository

import com.example.tripalert.data.mapper.TripMapper
import com.example.tripalert.data.remote.api.TripApi
import com.example.tripalert.domain.models.Trip
import com.example.tripalert.domain.repository.TripRepository
import com.example.tripalert.util.Resource

class TripRepositoryImpl(
    private val api: TripApi
) : TripRepository {

    override suspend fun getAllTrips(): Resource<List<Trip>> {
        return try {
            val dtoList = api.getAllTrips()
            val trips = dtoList.map { TripMapper.mapDtoToDomain(it) }
            Resource.Success(trips)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Ошибка получения поездок")
        }
    }

    override suspend fun getTripById(id: Long): Resource<Trip> {
        return try {
            val dto = api.getTripById(id)
            Resource.Success(TripMapper.mapDtoToDomain(dto))
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Ошибка загрузки поездки")
        }
    }

    override suspend fun createTrip(trip: Trip): Resource<Unit> {
        return try {
            val dto = TripMapper.mapDomainToCreateDto(trip)
            api.createTrip(dto)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Ошибка создания поездки")
        }
    }

    override suspend fun updateTrip(trip: Trip): Resource<Unit> {
        return try {
            val dto = TripMapper.mapDomainToUpdateDto(trip)
            api.updateTrip(trip.id, dto)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Ошибка обновления поездки")
        }
    }

    override suspend fun deleteTrip(id: Long): Resource<Unit> {
        return try {
            api.deleteTrip(id)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Ошибка удаления поездки")
        }
    }
}

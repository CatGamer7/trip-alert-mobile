package com.example.tripalert.data.repository

import com.example.tripalert.data.mapper.TripMapper
import com.example.tripalert.data.remote.api.TripApi
import com.example.tripalert.domain.models.Trip
import com.example.tripalert.domain.repository.TripRepository

class TripRepositoryImpl(
    private val api: TripApi,
    private val mapper: TripMapper
) : TripRepository {
    override suspend fun getTrips(): List<Trip> = api.getTrips().map { mapper.fromDto(it) }
    override suspend fun getTripById(id: Long): Trip = mapper.fromDto(api.getTripById(id))
    override suspend fun createTrip(trip: Trip): Trip = mapper.fromDto(api.createTrip(mapper.toCreateDto(trip)))
    override suspend fun updateTrip(trip: Trip): Trip = mapper.fromDto(api.updateTrip(trip.id, mapper.toUpdateDto(trip)))
    override suspend fun deleteTrip(id: Long) { api.deleteTrip(id) }
}
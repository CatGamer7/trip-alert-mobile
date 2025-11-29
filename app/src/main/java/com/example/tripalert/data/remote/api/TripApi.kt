package com.example.tripalert.data.remote.api

import com.example.tripalert.data.remote.dto.CreateTripDTO
import com.example.tripalert.data.remote.dto.TripResponseDTO
import com.example.tripalert.data.remote.dto.UpdateTripDTO
import retrofit2.http.*

interface TripApi {

    @GET("/api/trips")
    suspend fun getAllTrips(): List<TripResponseDTO>

    @GET("/api/trips/{id}")
    suspend fun getTripById(@Path("id") id: Long): TripResponseDTO

    @POST("/api/trips")
    suspend fun createTrip(@Body trip: CreateTripDTO)

    @PUT("/api/trips/{id}")
    suspend fun updateTrip(@Path("id") id: Long, @Body trip: UpdateTripDTO)

    @DELETE("/api/trips/{id}")
    suspend fun deleteTrip(@Path("id") id: Long)
}

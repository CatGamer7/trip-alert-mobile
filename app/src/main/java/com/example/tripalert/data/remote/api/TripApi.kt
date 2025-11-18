package com.example.tripalert.data.remote.api

import com.example.tripalert.data.remote.dto.CreateTripDTO
import com.example.tripalert.data.remote.dto.TripResponseDTO
import com.example.tripalert.data.remote.dto.UpdateTripDTO
import retrofit2.http.*

interface TripApi {

    @POST("trips")
    suspend fun createTrip(@Body trip: CreateTripDTO): TripResponseDTO

    @GET("trips")
    suspend fun getTrips(): List<TripResponseDTO>

    @GET("trips/{id}")
    suspend fun getTripById(@Path("id") id: Long): TripResponseDTO

    @PUT("trips/{id}")
    suspend fun updateTrip(@Path("id") id: Long, @Body updates: UpdateTripDTO): TripResponseDTO

    @DELETE("trips/{id}")
    suspend fun deleteTrip(@Path("id") id: Long)
}

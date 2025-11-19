package com.example.tripalert.data.remote

import com.example.tripalert.data.remote.dto.CreateTripDTO
import com.example.tripalert.data.remote.dto.TripResponseDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TripService {

    /**
     * Создание новой поездки.
     */
    @POST("api/trips")
    suspend fun createTrip(@Body trip: CreateTripDTO): TripResponseDTO

    /**
     * Получение деталей поездки по ID.
     */
    @GET("api/trips/{tripId}")
    suspend fun getTripDetails(@Path("tripId") tripId: Long): TripResponseDTO
}
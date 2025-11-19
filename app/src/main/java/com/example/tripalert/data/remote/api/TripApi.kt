package com.example.tripalert.data.remote.api

import com.example.tripalert.data.remote.dto.TripResponseDTO
import com.example.tripalert.data.remote.dto.CreateTripDTO
import com.example.tripalert.data.remote.dto.UpdateTripDTO
import retrofit2.http.*

interface TripApi {

    // GET: Используем TripResponseDTO для получения данных
    @GET("trips")
    suspend fun getTrips(@Query("userId") userId: Long): List<TripResponseDTO>

    @GET("trips/{id}")
    suspend fun getTripById(@Path("id") id: Long): TripResponseDTO

    // POST: Отправляем CreateTripDTO, получаем TripResponseDTO (с новым ID)
    @POST("trips")
    suspend fun createTrip(@Body trip: CreateTripDTO): TripResponseDTO

    // PUT: Отправляем UpdateTripDTO
    @PUT("trips/{id}")
    suspend fun updateTrip(@Path("id") id: Long, @Body trip: UpdateTripDTO)

    @DELETE("trips/{id}")
    suspend fun deleteTrip(@Path("id") id: Long)
}
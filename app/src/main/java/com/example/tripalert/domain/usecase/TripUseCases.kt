package com.example.tripalert.domain.usecase

import com.example.tripalert.domain.usecase.trip.CreateTripUseCase
import com.example.tripalert.domain.usecase.trip.DeleteTripUseCase
import com.example.tripalert.domain.usecase.trip.GetTripByIdUseCase
import com.example.tripalert.domain.usecase.trip.GetTripsUseCase
import com.example.tripalert.domain.usecase.trip.UpdateTripUseCase

data class TripUseCases(
    val createTrip: CreateTripUseCase,
    val updateTrip: UpdateTripUseCase,
    val getTripById: GetTripByIdUseCase,
    val getTrips: GetTripsUseCase,
    val deleteTrip: DeleteTripUseCase
)
package com.example.tripalert.ui.screens.tripdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripalert.domain.models.GeoPoint
import com.example.tripalert.domain.models.TransportType
import com.example.tripalert.domain.models.Trip
import com.example.tripalert.domain.repository.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TripDetailsViewModel(
    private val tripRepository: TripRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val defaultOrigin = GeoPoint(55.7558, 37.6173)      // Москва
    private val defaultDestination = GeoPoint(59.9343, 30.3351) // Санкт-Петербург

    private val _state = MutableStateFlow(
        TripDetailsState(
            trip = Trip(
                id = 0L,
                userId = 0L,
                name = "",
                origin = defaultOrigin,
                destination = defaultDestination,
                plannedTime = LocalDateTime.now(),
                transportType = TransportType.WALK,
                originAddress = "${defaultOrigin.latitude}, ${defaultOrigin.longitude}",
                destinationAddress = "${defaultDestination.latitude}, ${defaultDestination.longitude}"
            ),
            isSaving = false,
            saveSuccessful = false
        )
    )
    val state: StateFlow<TripDetailsState> = _state.asStateFlow()

    fun updateOriginAddress(address: String) {
        _state.value = _state.value.copy(
            trip = _state.value.trip.copy(
                originAddress = address
            )
        )
    }

    fun updateDestinationAddress(address: String) {
        _state.value = _state.value.copy(
            trip = _state.value.trip.copy(
                destinationAddress = address
            )
        )
    }

    fun updateOriginCoordinates(geoPoint: GeoPoint) {
        _state.value = _state.value.copy(
            trip = _state.value.trip.copy(
                origin = geoPoint,
                originAddress = "${geoPoint.latitude}, ${geoPoint.longitude}"
            )
        )
    }

    fun updateDestinationCoordinates(geoPoint: GeoPoint) {
        _state.value = _state.value.copy(
            trip = _state.value.trip.copy(
                destination = geoPoint,
                destinationAddress = "${geoPoint.latitude}, ${geoPoint.longitude}"
            )
        )
    }

    fun updatePlannedTime(dateTime: LocalDateTime) {
        _state.value = _state.value.copy(
            trip = _state.value.trip.copy(plannedTime = dateTime)
        )
    }

    fun updateTransportType(type: TransportType) {
        _state.value = _state.value.copy(
            trip = _state.value.trip.copy(transportType = type)
        )
    }

    fun saveTrip() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true)
            try {
                if (_state.value.trip.id == 0L) {
                    tripRepository.createTrip(_state.value.trip)
                } else {
                    tripRepository.updateTrip(_state.value.trip)
                }
                _state.value = _state.value.copy(isSaving = false, saveSuccessful = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isSaving = false)
            }
        }
    }

    fun saveComplete() {
        _state.value = _state.value.copy(saveSuccessful = false)
    }
}

data class TripDetailsState(
    val trip: Trip = getDefaultTrip(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaving: Boolean = false,
    val saveSuccessful: Boolean = false,
    val originAddressError: String? = null,
    val destinationAddressError: String? = null,
    val nameError: String? = null
)

private fun getDefaultTrip(): Trip = Trip(
    id = 1L,
    userId = 1L,
    name = "",
    origin = GeoPoint(0.0, 0.0),
    destination = GeoPoint(0.0, 0.0),
    plannedTime = LocalDateTime.now().plusHours(1),
    arrivalTime = null,
    transportType = TransportType.WALK,
    alertTime = null,
    originAddress = "",
    destinationAddress = ""
)

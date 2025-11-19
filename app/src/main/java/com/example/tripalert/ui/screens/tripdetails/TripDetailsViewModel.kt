package com.example.tripalert.ui.screens.tripdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripalert.domain.models.GeoPoint
import com.example.tripalert.domain.models.TransportType
import com.example.tripalert.domain.models.Trip
import com.example.tripalert.domain.repository.TripRepository
import com.example.tripalert.ui.navigation.TripAlertDestinations
import com.example.tripalert.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

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

private fun getDefaultTrip(): Trip {
    return Trip(
        userId = 1L, // ⚠️ Должен быть реальный ID пользователя
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
}


class TripDetailsViewModel(
    private val tripRepository: TripRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(TripDetailsState())
    val state: StateFlow<TripDetailsState> = _state.asStateFlow()

    private val currentTripId: Long? = savedStateHandle[TripAlertDestinations.TRIP_DETAILS_ID_KEY]

    init {
        if (currentTripId != null && currentTripId != -1L) {
            loadTrip(currentTripId)
        }
    }

    // --- Загрузка (режим редактирования) ---
    private fun loadTrip(tripId: Long) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = tripRepository.getTripById(tripId)) {
                is Resource.Success -> {
                    result.data?.let { trip ->
                        _state.value = _state.value.copy(
                            trip = trip,
                            isLoading = false
                        )
                    }
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Ошибка загрузки поездки: ${result.message}"
                    )
                }
                else -> Unit
            }
        }
    }

    // --- Обработчики ввода пользователя ---
    fun updateName(name: String) {
        _state.value = _state.value.copy(
            trip = _state.value.trip.copy(name = name),
            nameError = null
        )
    }

    fun updateOriginAddress(address: String) {
        _state.value = _state.value.copy(
            trip = _state.value.trip.copy(originAddress = address),
            originAddressError = null
        )
    }

    fun updateDestinationAddress(address: String) {
        _state.value = _state.value.copy(
            trip = _state.value.trip.copy(destinationAddress = address),
            destinationAddressError = null
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

    // --- Сохранение (Create/Update) ---
    fun saveTrip() {
        // ⚠️ Здесь должен быть вызов Geocoding!
        if (!validateTrip()) return

        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true)

            val tripToSave = _state.value.trip.copy(
                // ⚠️ Заглушка: Назначаем имя, если оно не было введено
                name = if (_state.value.trip.name.isBlank())
                    "Поездка в ${_state.value.trip.destinationAddress}"
                else _state.value.trip.name
            )

            val result = if (currentTripId != null && currentTripId != -1L) {
                tripRepository.updateTrip(tripToSave)
            } else {
                tripRepository.createTrip(tripToSave)
            }

            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(isSaving = false, saveSuccessful = true)
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isSaving = false,
                        error = "Не удалось сохранить: ${result.message}"
                    )
                }
                else -> Unit
            }
        }
    }

    // --- Валидация ---
    private fun validateTrip(): Boolean {
        var isValid = true
        var newOriginError: String? = null
        var newDestinationError: String? = null

        if (_state.value.trip.originAddress.isNullOrBlank()) {
            newOriginError = "Укажите место отправления."
            isValid = false
        }

        if (_state.value.trip.destinationAddress.isNullOrBlank()) {
            newDestinationError = "Укажите место назначения."
            isValid = false
        }

        _state.value = _state.value.copy(
            originAddressError = newOriginError,
            destinationAddressError = newDestinationError
        )
        return isValid
    }

    // --- Утилиты UI ---
    fun saveComplete() {
        _state.value = _state.value.copy(saveSuccessful = false)
    }

    fun errorShown() {
        _state.value = _state.value.copy(error = null)
    }
}
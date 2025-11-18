package com.example.tripalert.ui.screens.tripdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripalert.domain.models.GeoPoint
import com.example.tripalert.domain.models.Reminder
import com.example.tripalert.domain.models.TransportType
import com.example.tripalert.domain.models.Trip
import com.example.tripalert.domain.usecase.TripUseCases
import com.example.tripalert.domain.usecase.ReminderUseCases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

data class TripDetailsState(
    val isEditing: Boolean = false,
    val fromAddress: String = "",
    val toAddress: String = "",
    val date: String = "",          // "dd.MM.yyyy"
    val arrivalTime: String = "",   // "HH:mm"
    val departureTime: String = "", // "HH:mm"
    val reminderTime: String = "10 минут",
    val repeatReminder: String = "Не повторять" // новый параметр
)


class TripDetailsViewModel(
    private val tripUseCases: TripUseCases,
    private val reminderUseCases: ReminderUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(TripDetailsState())
    val state: StateFlow<TripDetailsState> = _state.asStateFlow()

    private val _errors = MutableSharedFlow<String>(replay = 1)
    val errors: SharedFlow<String> = _errors.asSharedFlow()

    private var currentTripId: Long = 0L
    private var currentUserId: Long = 1L // временный пользователь, замените на реальный из UserUseCases

    fun setEditing(editing: Boolean) {
        _state.update { it.copy(isEditing = editing) }
    }

    fun loadTrip(tripId: Long) {
        viewModelScope.launch {
            try {
                val trip = tripUseCases.getTripById(tripId)
                currentTripId = trip.id
                _state.update { s ->
                    s.copy(
                        isEditing = true,
                        fromAddress = trip.originAddress ?: "", // теперь раздельно
                        toAddress = trip.destinationAddress ?: "",
                        date = trip.plannedTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                        arrivalTime = trip.plannedTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                        departureTime = trip.alertTime?.format(DateTimeFormatter.ofPattern("HH:mm"))
                            ?: "",
                        reminderTime = trip.alertTime?.let { "по расписанию" } ?: s.reminderTime
                    )
                }
            } catch (e: Exception) {
                _errors.emit("Не удалось загрузить поездку: ${e.message ?: e::class.simpleName}")
            }
        }
    }

    fun updateField(
        from: String? = null,
        to: String? = null,
        date: String? = null,
        arrival: String? = null,
        departure: String? = null,
        reminderTime: String? = null
    ) {
        _state.update { old ->
            old.copy(
                fromAddress = from ?: old.fromAddress,
                toAddress = to ?: old.toAddress,
                date = date ?: old.date,
                arrivalTime = arrival ?: old.arrivalTime,
                departureTime = departure ?: old.departureTime,
                reminderTime = reminderTime ?: old.reminderTime
            )
        }
    }

    fun calculateDeparture(arrival: String, durationMinutes: Int) {
        try {
            val parts = arrival.split(":").map { it.toIntOrNull() ?: 0 }
            val cal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, parts.getOrNull(0) ?: 0)
                set(Calendar.MINUTE, parts.getOrNull(1) ?: 0)
                add(Calendar.MINUTE, -durationMinutes)
            }
            val dep =
                String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
            updateField(departure = dep)
        } catch (t: Throwable) {
            viewModelScope.launch { _errors.emit("Ошибка расчёта времени: ${t.message ?: t::class.simpleName}") }
        }
    }

    fun saveTrip(onResult: (Boolean) -> Unit = {}) {
        val trip = buildTripFromState()
        viewModelScope.launch {
            try {
                if (trip.id == 0L) {
                    tripUseCases.createTrip(trip)
                } else {
                    tripUseCases.updateTrip(trip)
                }
                onResult(true)
            } catch (e: Exception) {
                _errors.emit("Ошибка сохранения: ${e.message ?: e::class.simpleName}")
                onResult(false)
            }
        }
    }

    fun deleteTrip(onResult: (Boolean) -> Unit = {}) {
        if (currentTripId == 0L) {
            onResult(false)
            return
        }

        viewModelScope.launch {
            try {
                tripUseCases.deleteTrip(currentTripId)
                onResult(true)
            } catch (e: Exception) {
                _errors.emit("Ошибка удаления: ${e.message ?: e::class.simpleName}")
                onResult(false)
            }
        }
    }

    private fun buildTripFromState(): Trip {
        val dateParts = state.value.date.split(".")
        val timeParts = state.value.arrivalTime.split(":")

        val arrivalDateTime = if (dateParts.size == 3 && timeParts.size == 2) {
            LocalDateTime.of(
                dateParts[2].toInt(),
                dateParts[1].toInt(),
                dateParts[0].toInt(),
                timeParts[0].toInt(),
                timeParts[1].toInt()
            )
        } else null

        val plannedTime = arrivalDateTime?.minusMinutes(17)

        val originPoint = GeoPoint(0.0, 0.0)
        val destinationPoint = GeoPoint(0.0, 0.0)

        val alertMinutes = when (state.value.reminderTime) {
            "5 минут" -> 5L
            "10 минут" -> 10L
            "15 минут" -> 15L
            "30 минут" -> 30L
            "1 час" -> 60L
            else -> 15L
        }

        val alertTime = arrivalDateTime?.minusMinutes(alertMinutes)

        return Trip(
            id = currentTripId,
            userId = currentUserId,
            name = "${state.value.fromAddress} → ${state.value.toAddress}",
            origin = originPoint,
            destination = destinationPoint,
            plannedTime = plannedTime ?: LocalDateTime.now(),
            arrivalTime = arrivalDateTime,
            transportType = TransportType.WALK,
            alertTime = alertTime
        )
    }
}


package com.example.tripalert.ui.screens.triplist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripalert.domain.models.Trip
import com.example.tripalert.domain.usecase.trip.GetTripsUseCase
import com.example.tripalert.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class TripListState(
    val dailyTrips: List<Trip> = emptyList(),
    val otherTrips: List<Trip> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class TripListViewModel(
    private val getTripsUseCase: GetTripsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TripListState(isLoading = true))
    val state: StateFlow<TripListState> = _state.asStateFlow()

    init {
        loadTrips()
    }

    private fun loadTrips() {
        viewModelScope.launch {
            getTripsUseCase().collect { resource ->
                when (resource) {

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }

                    is Resource.Error -> {
                        _state.value = TripListState(
                            error = resource.message,
                            isLoading = false
                        )
                    }

                    is Resource.Success -> {
                        val trips = resource.data ?: emptyList()
                        val today = LocalDate.now()

                        val daily = trips.filter { it.plannedTime.toLocalDate() == today }
                        val other = trips.filter { it.plannedTime.toLocalDate() != today }

                        _state.value = TripListState(
                            dailyTrips = daily,
                            otherTrips = other,
                            isLoading = false
                        )
                    }

                    else -> {}
                }
            }
        }
    }

    fun errorShown() {
        _state.value = _state.value.copy(error = null)
    }
}

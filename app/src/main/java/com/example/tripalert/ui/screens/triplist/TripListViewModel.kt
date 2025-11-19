package com.example.tripalert.ui.screens.triplist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripalert.domain.models.Trip
import com.example.tripalert.domain.repository.TripRepository
import com.example.tripalert.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

// Класс состояния, который UI будет слушать
data class TripListState(
    val dailyTrips: List<Trip> = emptyList(),
    val otherTrips: List<Trip> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class TripListViewModel(
    private val tripRepository: TripRepository
) : ViewModel() {

    // Приватный MutableStateFlow для изменения состояния
    private val _state = MutableStateFlow(TripListState())
    // Публичный StateFlow для потребления UI
    val state: StateFlow<TripListState> = _state.asStateFlow()

    // ⚠️ ВАЖНО: В реальном проекте этот ID должен приходить из UserSession
    private val currentUserId: Long = 1L

    init {
        loadTrips()
    }

    fun loadTrips() {
        // Вызываем функцию репозитория, которая возвращает Flow<Resource<List<Trip>>>
        tripRepository.getTrips(currentUserId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = result.isLoading,
                        // Оставляем старые данные или показываем данные из кэша, если они есть
                        dailyTrips = splitTrips(result.data).first,
                        otherTrips = splitTrips(result.data).second,
                        error = null
                    )
                }
                is Resource.Success -> {
                    // Разделяем поездки на две категории
                    val (daily, other) = splitTrips(result.data)
                    _state.value = _state.value.copy(
                        dailyTrips = daily,
                        otherTrips = other,
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }.launchIn(viewModelScope) // Запускаем Flow в скоупе ViewModel
    }

    // Вспомогательная функция для разделения поездок
    private fun splitTrips(trips: List<Trip>?): Pair<List<Trip>, List<Trip>> {
        if (trips.isNullOrEmpty()) return Pair(emptyList(), emptyList())

        // ⚠️ ЗАГЛУШКА: Здесь должна быть реальная логика, основанная на свойствах Trip (например, isDaily)
        // Пока делим пополам для демонстрации
        val dailyCount = if (trips.size > 2) trips.size / 2 else trips.size
        return Pair(
            trips.take(dailyCount),
            trips.drop(dailyCount)
        )
    }

    // Функция, которую будет вызывать UI для сброса ошибки (после показа Snackbar)
    fun errorShown() {
        _state.value = _state.value.copy(error = null)
    }
}
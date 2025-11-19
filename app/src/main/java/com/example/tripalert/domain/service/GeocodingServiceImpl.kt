// com.example.tripalert.data.service/GeocodingServiceImpl.kt

package com.example.tripalert.data.service

import com.example.tripalert.domain.models.GeoPoint
import com.example.tripalert.domain.service.GeocodingService
import com.example.tripalert.util.Resource
import kotlinx.coroutines.delay

class GeocodingServiceImpl : GeocodingService {

    // Имитация задержки сетевого запроса
    private val fakeDelay = 300L

    // ⚠️ ВАЖНО: В Mapbox координаты обычно возвращаются в формате (Lat, Lon)
    override suspend fun getCoordinatesFromAddress(address: String): Resource<GeoPoint> {
        delay(fakeDelay)

        return when {
            address.contains("Москва", ignoreCase = true) -> {
                Resource.Success(GeoPoint(latitude = 55.7558, longitude = 37.6176))
            }
            address.contains("Петербург", ignoreCase = true) -> {
                Resource.Success(GeoPoint(latitude = 59.9343, longitude = 30.3351))
            }
            address.isBlank() -> {
                Resource.Error("Адрес не может быть пустым.")
            }
            else -> {
                // Если Mapbox вернет ошибку или ничего не найдет
                Resource.Error("Адрес '$address' не найден. (Заглушка GeocodingServiceImpl)")
            }
        }
    }

    override suspend fun getAddressFromCoordinates(point: GeoPoint): Resource<String> {
        delay(fakeDelay)
        return Resource.Success("Адрес по координатам: ${String.format("%.4f", point.latitude)}, ${String.format("%.4f", point.longitude)}")
    }
}
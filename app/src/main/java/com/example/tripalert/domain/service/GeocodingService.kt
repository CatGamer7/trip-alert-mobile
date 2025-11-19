// com.example.tripalert.domain.service/GeocodingService.kt

package com.example.tripalert.domain.service

import com.example.tripalert.domain.models.GeoPoint
import com.example.tripalert.util.Resource

/**
 * Сервис для преобразования адресов в координаты и наоборот (Геокодирование).
 * Будет реализован с помощью Mapbox, Google Geocoding API или Android Geocoder.
 */
interface GeocodingService {

    // Преобразует текстовый адрес в координаты
    suspend fun getCoordinatesFromAddress(address: String): Resource<GeoPoint>

    // Преобразует координаты в текстовый адрес (обратное геокодирование)
    suspend fun getAddressFromCoordinates(point: GeoPoint): Resource<String>
}
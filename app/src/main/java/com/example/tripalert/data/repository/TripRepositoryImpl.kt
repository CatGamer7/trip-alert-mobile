package com.example.tripalert.data.repository

import com.example.tripalert.data.local.dao.TripDao
import com.example.tripalert.data.mapper.TripMapper
import com.example.tripalert.data.remote.api.TripApi
import com.example.tripalert.domain.models.Trip
import com.example.tripalert.domain.repository.TripRepository
import com.example.tripalert.domain.service.GeocodingService
import com.example.tripalert.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class TripRepositoryImpl(
    private val api: TripApi,
    private val dao: TripDao,
    private val mapper: TripMapper,
    private val geocodingService: GeocodingService
) : TripRepository {

    /**
     * Преобразует текстовые адреса в координаты с помощью GeocodingService.
     * Добавляет проверку на null/пустоту.
     */
    private suspend fun geocodeTrip(trip: Trip): Resource<Trip> {

        // ✅ ИСПРАВЛЕНИЕ: Получаем String? из Trip и проверяем на пустоту/null.
        val originAddress = trip.originAddress
        if (originAddress.isNullOrBlank()) {
            return Resource.Error("Адрес отправления не может быть пустым.")
        }

        val destinationAddress = trip.destinationAddress
        if (destinationAddress.isNullOrBlank()) {
            return Resource.Error("Адрес назначения не может быть пустым.")
        }

        // 1. Геокодирование пункта отправления (Теперь передаем не-null String)
        val originResource = geocodingService.getCoordinatesFromAddress(originAddress)
        if (originResource is Resource.Error || originResource.data == null) {
            return Resource.Error("Не удалось найти координаты для адреса отправления: ${originResource.message}")
        }

        // 2. Геокодирование пункта назначения (Теперь передаем не-null String)
        val destinationResource = geocodingService.getCoordinatesFromAddress(destinationAddress)
        if (destinationResource is Resource.Error || destinationResource.data == null) {
            return Resource.Error("Не удалось найти координаты для адреса назначения: ${destinationResource.message}")
        }

        // 3. Возвращаем копию поездки с заполненными координатами
        return Resource.Success(
            trip.copy(
                origin = originResource.data,
                destination = destinationResource.data
            )
        )
    }

    // --- GET TRIPS (Source of Truth: Cache-First) ---
    override fun getTrips(userId: Long): Flow<Resource<List<Trip>>> = flow {

        emit(Resource.Loading(isLoading = true))

        val localTrips = try {
            dao.getTripsByUserId(userId).map { mapper.mapEntityToDomain(it) }
        } catch (e: Exception) {
            emptyList()
        }

        if (localTrips.isNotEmpty()) {
            emit(Resource.Loading(data = localTrips, isLoading = true))
        }

        try {
            val remoteTripDtos = api.getTrips(userId)
            val remoteTrips = remoteTripDtos.map { mapper.mapDtoToDomain(it) }

            dao.clearAndInsertTrips(remoteTripDtos.map { mapper.mapDtoToEntity(it) })

            emit(Resource.Success(remoteTrips))

        } catch (e: HttpException) {
            emit(Resource.Error(
                message = "Ошибка сети (${e.code()}). Проверьте сервер.",
                data = localTrips
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "Не удалось связаться с сервером. Проверьте интернет.",
                data = localTrips
            ))
        } finally {
            emit(Resource.Loading(isLoading = false))
        }
    }

    // --- CREATE TRIP ---
    override suspend fun createTrip(trip: Trip): Resource<Trip> {
        // 1. Сначала геокодируем адреса
        val geocodeResult = geocodeTrip(trip)
        if (geocodeResult is Resource.Error) {
            return geocodeResult
        }
        val tripWithCoords = geocodeResult.data!!

        // 2. Далее стандартный вызов API
        return try {
            val createDto = mapper.mapDomainToCreateDto(tripWithCoords)
            val responseDto = api.createTrip(createDto)

            val createdTrip = mapper.mapDtoToDomain(responseDto)
            dao.insertTrip(mapper.mapDtoToEntity(responseDto))

            Resource.Success(createdTrip)
        } catch (e: HttpException) {
            Resource.Error("Ошибка сервера при создании поездки: ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Ошибка подключения при создании поездки.")
        }
    }

    // --- UPDATE TRIP ---
    override suspend fun updateTrip(trip: Trip): Resource<Unit> {
        // 1. Сначала геокодируем адреса
        val geocodeResult = geocodeTrip(trip)
        if (geocodeResult is Resource.Error) {
            return Resource.Error(geocodeResult.message ?: "Ошибка геокодирования при обновлении.")
        }
        val tripWithCoords = geocodeResult.data!!

        // 2. Далее стандартный вызов API
        return try {
            val updateDto = mapper.mapDomainToUpdateDto(tripWithCoords)
            api.updateTrip(trip.id, updateDto)

            dao.updateTrip(mapper.mapDomainToEntity(tripWithCoords))

            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error("Ошибка сервера при обновлении поездки: ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Ошибка подключения при обновлении поездки.")
        }
    }

    // --- GET TRIP BY ID ---
    override suspend fun getTripById(tripId: Long): Resource<Trip> {
        // ... (код остается прежним)
        val localTrip = try {
            dao.getTripById(tripId)?.let { mapper.mapEntityToDomain(it) }
        } catch (e: Exception) {
            null
        }

        if (localTrip != null) return Resource.Success(localTrip)

        return try {
            val dto = api.getTripById(tripId)
            val trip = mapper.mapDtoToDomain(dto)
            dao.insertTrip(mapper.mapDtoToEntity(dto))
            Resource.Success(trip)
        } catch (e: HttpException) {
            Resource.Error("Ошибка загрузки поездки: ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Ошибка подключения при загрузке поездки.")
        }
    }

    // --- DELETE TRIP ---
    override suspend fun deleteTrip(tripId: Long): Resource<Unit> {
        // ... (код остается прежним)
        return try {
            api.deleteTrip(tripId)
            dao.deleteTrip(tripId)
            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error("Ошибка сервера при удалении поездки: ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Ошибка подключения при удалении поездки.")
        }
    }
}
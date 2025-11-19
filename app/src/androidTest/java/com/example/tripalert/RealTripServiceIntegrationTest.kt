package com.example.tripalert

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tripalert.data.remote.TripService
import com.example.tripalert.data.remote.api.LocalDateTimeAdapter
import com.example.tripalert.data.remote.dto.CoordinateDTO
import com.example.tripalert.data.remote.dto.CreateTripDTO
import com.example.tripalert.data.remote.dto.TripResponseDTO
import com.example.tripalert.data.remote.gson.GeoJsonPointAdapter
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException
import retrofit2.Retrofit
// Используем GsonConverterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

// ВАЖНО: Тест должен быть запущен как Instrumented Test (на эмуляторе/устройстве)
@RunWith(AndroidJUnit4::class)
class RealTripServiceIntegrationTest {

    private lateinit var tripService: TripService

    // !!! ЗАМЕНИТЕ на реальный базовый URL вашего бэкенда !!!
    private val REAL_BASE_URL = "https://localhost:8080/api/users"

    // ID поездки, которая должна существовать на вашем тестовом бэкенде
    private val TEST_EXISTING_TRIP_ID = 1L

    // ID, который гарантированно не существует (для теста 404)
    private val TEST_NON_EXISTENT_TRIP_ID = 999999L

    @Before
    fun setup() {
        // Настройка Gson с адаптерами, как в RetrofitInstance
        val gson = GsonBuilder()
            // 1. Адаптер для LocalDateTime
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            // 2. Адаптер для GeoJSON CoordinateDTO
            .registerTypeAdapter(CoordinateDTO::class.java, GeoJsonPointAdapter())
            .create()

        // Создаем Retrofit-клиент, настроенный на реальный бэкенд
        tripService = Retrofit.Builder()
            .baseUrl(REAL_BASE_URL)
            // ИСПОЛЬЗУЕМ GsonConverterFactory
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TripService::class.java)
    }

    /**
     * Тест 1: Проверка успешного создания новой поездки (POST) и корректности DTO.
     */
    @Test
    fun test_createTrip_success_shouldReturnNewTrip() = runBlocking {
        // Подготовка тестовых данных
        val originCoord = CoordinateDTO(x = 37.6175, y = 55.7512) // Москва: Longitude(x), Latitude(y)
        val destCoord = CoordinateDTO(x = 30.3158, y = 59.9388) // Санкт-Петербург: Longitude(x), Latitude(y)
        // Убираем наносекунды для сравнения, так как сериализация/десериализация может их отбрасывать
        val plannedTime = LocalDateTime.now().plusDays(1).withNano(0)

        val newTripDto = CreateTripDTO(
            userId = 123L, // Предполагаемый тестовый пользователь
            name = "Тестовая поездка в Петербург",
            origin = originCoord,
            destination = destCoord,
            plannedTime = plannedTime,
            transportType = "TRAIN",
            alertTime = null
        )

        val createdTrip: TripResponseDTO = try {
            tripService.createTrip(newTripDto)
        } catch (e: Exception) {
            throw AssertionError("Не удалось создать поездку. Проверьте базовый URL, сеть и статус сервера. Ошибка: ${e.message}", e)
        }

        // Проверки (Assertions)
        assertNotNull("TripResponseDTO не должен быть null", createdTrip)
        assertTrue("Созданный ID должен быть больше 0", createdTrip.id > 0)
        assertEquals(newTripDto.name, createdTrip.name)
        assertEquals(newTripDto.transportType, createdTrip.transportType)

        // Проверка корректной десериализации LocalDateTime
        assertEquals(newTripDto.plannedTime, createdTrip.plannedTime.withNano(0))

        // Проверка вложенного DTO (с учетом возможной погрешности float/double)
        assertEquals("Проверка Longitude", originCoord.x, createdTrip.origin.x, 0.0001)

        // (Очистка): В реальном тесте здесь нужно вызвать метод DELETE для удаления созданной поездки.
    }

    /**
     * Тест 2: Проверка успешного получения существующей поездки (GET) с корректной датой/временем.
     */
    @Test
    fun test_getTripDetails_existingId_shouldReturnTripAndParseDateTime() = runBlocking {
        val trip: TripResponseDTO = try {
            tripService.getTripDetails(tripId = TEST_EXISTING_TRIP_ID)
        } catch (e: Exception) {
            throw AssertionError("Не удалось получить детали существующей поездки с реального бэкенда. Ошибка: ${e.message}", e)
        }

        // Проверки (Assertions)
        assertNotNull("Объект поездки (DTO) не должен быть null", trip)
        assertTrue("ID поездки должен совпадать с запрошенным ID", trip.id == TEST_EXISTING_TRIP_ID)
        // Проверяем, что LocalDateTime успешно десериализован
        assertNotNull("Planned Time не должен быть null", trip.plannedTime)
        assertTrue("Latitude (y) должен находиться в допустимом диапазоне", trip.destination.y in -90.0..90.0)
    }

    /**
     * Тест 3: Проверка обработки несуществующего ресурса (ожидается HTTP 404).
     */
    @Test
    fun test_getTripDetails_nonExistentId_shouldThrowHttpException404() = runBlocking {
        var exceptionCaught = false
        var httpCode = 0

        try {
            tripService.getTripDetails(tripId = TEST_NON_EXISTENT_TRIP_ID)
        } catch (e: HttpException) {
            exceptionCaught = true
            httpCode = e.code()
        } catch (e: Exception) {
            throw AssertionError("Перехвачено неожиданное исключение типа: ${e::class.simpleName}", e)
        }

        assertTrue("Ожидалось исключение HttpException", exceptionCaught)
        assertEquals("Ожидался HTTP-код 404 (Not Found), но получен $httpCode", 404, httpCode)
    }
}
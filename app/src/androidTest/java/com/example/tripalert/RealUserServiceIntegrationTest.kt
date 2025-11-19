package com.example.tripalert

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tripalert.data.remote.UserService
import com.example.tripalert.data.remote.dto.CreateUserDTO
import com.example.tripalert.data.remote.dto.UserResponseDTO
import com.example.tripalert.data.remote.dto.UpdateUserDTO
import com.example.tripalert.data.remote.api.LocalDateTimeAdapter
import com.example.tripalert.data.remote.dto.CoordinateDTO // Оставляем, если нужен для GsonBuilder
import com.example.tripalert.data.remote.gson.GeoJsonPointAdapter // Оставляем, если нужен для GsonBuilder
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

// ВАЖНО: Тест должен быть запущен как Instrumented Test (на эмуляторе/устройстве)
@RunWith(AndroidJUnit4::class)
class RealUserServiceIntegrationTest {

    private lateinit var userService: UserService

    // !!! ЗАМЕНИТЕ на реальный базовый URL вашего бэкенда !!!
    // Напоминание: 10.0.2.2 - это IP для доступа к localhost с Android-эмулятора.
    private val REAL_BASE_URL = "https://localhost:8080/api/users"

    // ID пользователя, который должен существовать на вашем тестовом бэкенде
    private val TEST_EXISTING_USER_ID = 1L

    // ID, который гарантированно не существует (для теста 404)
    private val TEST_NON_EXISTENT_USER_ID = 999999L

    @Before
    fun setup() {
        // Настройка Gson с адаптерами
        val gson = GsonBuilder()
            // 1. Адаптер для LocalDateTime
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            // 2. Адаптер для GeoJSON CoordinateDTO (если он используется в приложении)
            .registerTypeAdapter(CoordinateDTO::class.java, GeoJsonPointAdapter())
            .create()

        // Создаем Retrofit-клиент
        userService = Retrofit.Builder()
            .baseUrl(REAL_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UserService::class.java)
    }

    /**
     * Тест 1: Проверка успешного создания нового пользователя (POST) и корректности DTO.
     */
    @Test
    fun test_createUser_success_shouldReturnNewUser() = runBlocking {
        // Подготовка тестовых данных
        val uniqueUsername = "test_user_${System.currentTimeMillis()}"
        val uniqueEmail = "test_${System.currentTimeMillis()}@example.com"

        val newUserDto = CreateUserDTO(
            username = uniqueUsername,
            password = "secure_password_123",
            email = uniqueEmail,
            timeOffset = 180, // +3 часа (в минутах)
            preferredTransport = 2 // Например, 'Поезд'
        )

        val createdUser: UserResponseDTO = try {
            // Вызываем POST /api/users
            userService.createUser(newUserDto)
        } catch (e: Exception) {
            throw AssertionError("Не удалось создать пользователя. Проверьте URL, сеть и статус сервера. Ошибка: ${e.message}", e)
        }

        // Проверки (Assertions)
        assertNotNull("UserResponseDTO не должен быть null", createdUser)
        assertTrue("Созданный ID должен быть больше 0", createdUser.id > 0)
        assertEquals(newUserDto.username, createdUser.username)
        assertEquals(newUserDto.email, createdUser.email)
        assertEquals(newUserDto.timeOffset, createdUser.timeOffset)
        assertEquals(newUserDto.preferredTransport, createdUser.preferredTransport)

        // (Очистка): В идеальном тесте здесь нужно удалить созданный ресурс.
    }

    /**
     * Тест 2: Проверка успешного получения существующего пользователя (GET).
     */
    @Test
    fun test_getUserDetails_existingId_shouldReturnUser() = runBlocking {
        val user: UserResponseDTO = try {
            // Вызываем GET /api/users/{TEST_EXISTING_USER_ID}
            userService.getUserDetails(userId = TEST_EXISTING_USER_ID)
        } catch (e: Exception) {
            throw AssertionError("Не удалось получить детали существующего пользователя. Ошибка: ${e.message}", e)
        }

        // Проверки (Assertions)
        assertNotNull("Объект пользователя (DTO) не должен быть null", user)
        assertTrue("ID пользователя должен совпадать с запрошенным ID", user.id == TEST_EXISTING_USER_ID)
        assertNotNull("Username не должен быть null", user.username)

        // Дополнительные проверки, специфичные для вашей модели данных
        assertTrue("timeOffset должен быть в допустимом диапазоне", user.timeOffset in -720..720)
    }

    /**
     * Тест 3: Проверка успешного обновления части данных пользователя (PUT/PATCH).
     * ВАЖНО: Требует, чтобы бэкенд поддерживал обновление и корректно возвращал обновленный DTO.
     */
    @Test
    fun test_updateUser_success_shouldChangeTimeOffset() = runBlocking {
        // 1. Получаем текущие данные для проверки
        val originalUser = userService.getUserDetails(userId = TEST_EXISTING_USER_ID)
        val originalOffset = originalUser.timeOffset
        val newOffset = originalOffset + 60 // Сдвигаем на 1 час вперед

        // 2. Создаем DTO для обновления, меняя только timeOffset
        val updateDto = UpdateUserDTO(
            timeOffset = newOffset,
            // Другие поля остаются null
        )

        // 3. Вызываем PUT /api/users/{TEST_EXISTING_USER_ID}
        val updatedUser: UserResponseDTO = try {
            userService.updateUser(TEST_EXISTING_USER_ID, updateDto)
        } catch (e: Exception) {
            throw AssertionError("Не удалось обновить пользователя. Проверьте реализацию PUT/PATCH на бэкенде. Ошибка: ${e.message}", e)
        }

        // 4. Проверки
        assertNotNull("Обновленный DTO не должен быть null", updatedUser)
        // Проверяем, что смещение времени действительно изменилось
        assertNotEquals("Смещение времени должно измениться", originalOffset, updatedUser.timeOffset)
        assertEquals("Смещение времени должно быть равно новому значению", newOffset, updatedUser.timeOffset)

        // Проверяем, что другие поля (например, email) остались прежними
        assertEquals("Email не должен был измениться при частичном обновлении", originalUser.email, updatedUser.email)

        // (Очистка): Откат изменений, чтобы не сломать следующие тесты.
        // Это важный этап в интеграционных тестах.
        val rollbackDto = UpdateUserDTO(timeOffset = originalOffset)
        userService.updateUser(TEST_EXISTING_USER_ID, rollbackDto)
    }


    /**
     * Тест 4: Проверка обработки несуществующего ресурса (ожидается HTTP 404).
     */
    @Test
    fun test_getUserDetails_nonExistentId_shouldThrowHttpException404() = runBlocking {
        var exceptionCaught = false
        var httpCode = 0

        try {
            userService.getUserDetails(userId = TEST_NON_EXISTENT_USER_ID)
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
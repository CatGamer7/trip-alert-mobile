package com.example.tripalert.data.remote

import com.example.tripalert.data.remote.dto.CreateUserDTO
import com.example.tripalert.data.remote.dto.UpdateUserDTO
import com.example.tripalert.data.remote.dto.UserResponseDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Retrofit-интерфейс для взаимодействия с эндпоинтами управления пользователями.
 */
interface UserService {

    // POST /api/users
    // Создает нового пользователя.
    @POST(".")
    suspend fun createUser(@Body user: CreateUserDTO): UserResponseDTO

    // GET /api/users/{userId}
    // Получает детали существующего пользователя.
    @GET("{userId}")
    suspend fun getUserDetails(@Path("userId") userId: Long): UserResponseDTO

    // PUT /api/users/{userId}
    // Обновляет существующего пользователя. Используем PUT для полной замены ресурса,
    // но в DTO используем nullable-поля, чтобы можно было отправлять частичные обновления (PATCH).
    @PUT("{userId}")
    suspend fun updateUser(
        @Path("userId") userId: Long,
        @Body user: UpdateUserDTO
    ): UserResponseDTO
}
package com.example.tripalert.data.remote.api

import com.example.tripalert.data.remote.dto.CreateUserDTO
import com.example.tripalert.data.remote.dto.UpdateUserDTO
import com.example.tripalert.data.remote.dto.UserResponseDTO
import retrofit2.http.*

interface UserApi {

    @POST("users")
    suspend fun createUser(@Body user: CreateUserDTO): UserResponseDTO

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Long): UserResponseDTO

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: Long, @Body updates: UpdateUserDTO): UserResponseDTO

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Long)
}

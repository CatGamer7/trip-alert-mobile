package com.example.tripalert.data.remote.api

import com.example.tripalert.data.remote.dto.AuthResponseDTO
import com.example.tripalert.data.remote.dto.CreateUserDTO
import com.example.tripalert.data.remote.dto.LoginRequestDTO
import com.example.tripalert.data.remote.dto.UpdateUserDTO
import com.example.tripalert.data.remote.dto.UserResponseDTO
import okhttp3.ResponseBody
import retrofit2.http.*

interface UserApi {

    @POST("/api/users")
    suspend fun createUser(@Body user: CreateUserDTO): ResponseBody

    @POST("/api/login")
    suspend fun login(@Body credentials: LoginRequestDTO): ResponseBody

    @GET("/api/users/{id}")
    suspend fun getUserById(
        @Path("id") id: Long
    ): UserResponseDTO

    @PUT("/api/users/{id}")
    suspend fun updateUser(
        @Path("id") id: Long,
        @Body updates: UpdateUserDTO
    ): UserResponseDTO

    @DELETE("/api/users/{id}")
    suspend fun deleteUser(
        @Path("id") id: Long
    )
}
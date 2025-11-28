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
    suspend fun createUser(@Body user: CreateUserDTO): AuthResponseDTO // Возврат AuthResponseDTO для токена


    @POST("/api/login")
    suspend fun login(@Body credentials: LoginRequestDTO): AuthResponseDTO // Возврат AuthResponseDTO для токена


    @GET("/api/users/{username}")
    suspend fun getUserByUsername(
        @Path("username") username: String
    ): UserResponseDTO


    @PUT("/api/users/{username}")
    suspend fun updateUser(
        @Path("username") username: String,
        @Body updates: UpdateUserDTO
    ): UserResponseDTO


    @DELETE("/api/users/{username}")
    suspend fun deleteUser(
        @Path("username") username: String
    )
}
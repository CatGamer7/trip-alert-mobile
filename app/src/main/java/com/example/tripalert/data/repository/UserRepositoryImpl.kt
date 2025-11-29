package com.example.tripalert.data.repository

import com.example.tripalert.data.mapper.UserMapper
import com.example.tripalert.data.remote.api.UserApi
import com.example.tripalert.data.remote.dto.CreateUserDTO
import com.example.tripalert.data.remote.dto.LoginRequestDTO
import com.example.tripalert.data.remote.dto.UpdateUserDTO
import com.example.tripalert.domain.models.TransportType
import com.example.tripalert.domain.models.User
import com.example.tripalert.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.HttpException

class UserRepositoryImpl(private val api: UserApi) : UserRepository {

    private val _userFlow = MutableStateFlow<User?>(null)
    private var authToken: String? = null
    private var currentUsername: String? = null

    override fun getUserProfileFlow(): Flow<User?> = _userFlow.asStateFlow()

    override fun getCurrentUserName(): String? = currentUsername

    override fun getCurrentAuthToken(): String? =
        authToken



    private fun handleAuthResponse(token: String, userDto: com.example.tripalert.data.remote.dto.UserResponseDTO) {
        if (token.isBlank()) {
            throw Exception("Server returned empty token")
        }


        authToken = token
        currentUsername = userDto.username

        _userFlow.value = UserMapper.fromDto(userDto, "password_hidden")
    }

    override suspend fun signIn(username: String, password: String) {
        val request = LoginRequestDTO(username, password)

        val response = api.login(request)

        handleAuthResponse(response.token, response.user)
    }

    override suspend fun createUser(username: String, password: String, timeOffset: Int, preferredTransport: TransportType) {
        val dto = CreateUserDTO(username, password, timeOffset, preferredTransport)

        val response = api.createUser(dto)

        handleAuthResponse(response.token, response.user)
    }

    override suspend fun fetchProfileFromServer() {

        val tokenCheck = authToken ?: throw IllegalStateException("Not authenticated (No token)")
        val username = currentUsername ?: throw IllegalStateException("No username set")

        try {

            val response = api.getUserByUsername(username)
            _userFlow.value = UserMapper.fromDto(response, "password_hidden")

        } catch (e: Exception) {
            val status = if (e is HttpException) e.code() else "Network/Timeout"


            authToken = null
            currentUsername = null
            _userFlow.value = null

            throw Exception("Profile Fetch Failed. Status: $status. ${e.message}", e)
        }
    }

    override suspend fun updateProfile(timeOffset: Int?, preferredTransport: TransportType?) {
        val tokenCheck = authToken ?: throw IllegalStateException("Not authenticated")
        val username = currentUsername ?: throw IllegalStateException("No username")

        val updateDto = UpdateUserDTO(
            username = username,
            timeOffset = timeOffset,
            preferredTransport = preferredTransport
        )

        val response = api.updateUser(username, updateDto)
        _userFlow.value = UserMapper.fromDto(response, "hidden")
    }

    override suspend fun deleteUser() {
        val tokenCheck = authToken ?: throw IllegalStateException("Not authenticated")
        val username = currentUsername ?: throw IllegalStateException("No username")

        api.deleteUser(username)

        authToken = null
        currentUsername = null
        _userFlow.value = null
    }

    override suspend fun signOut() {
        authToken = null
        currentUsername = null
        _userFlow.value = null
    }
}
package com.example.tripalert.data.repository

import com.example.tripalert.data.mapper.UserMapper
import com.example.tripalert.data.remote.api.UserApi
import com.example.tripalert.data.remote.dto.AuthResponseDTO
import com.example.tripalert.data.remote.dto.CreateUserDTO
import com.example.tripalert.data.remote.dto.LoginRequestDTO
import com.example.tripalert.data.remote.dto.UpdateUserDTO
import com.example.tripalert.domain.models.User
import com.example.tripalert.domain.repository.UserRepository
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.ResponseBody
import retrofit2.HttpException

class UserRepositoryImpl(private val api: UserApi, private val gson: Gson) : UserRepository {

    private val _userFlow = MutableStateFlow<User?>(null)
    private var authToken: String? = null
    private var currentUserId: Long? = null
    private val UNKNOWN_USER_PLACEHOLDER = "temp_unknown_user_placeholder"

    override fun getUserProfileFlow(): Flow<User?> = _userFlow.asStateFlow()
    override fun getCurrentUserName(): String? = _userFlow.value?.username
    override fun getCurrentAuthToken(): String? = authToken

    private fun parseAuthResponse(responseBody: ResponseBody): AuthResponseDTO {
        val responseString = responseBody.string()
        try {
            return gson.fromJson(responseString, AuthResponseDTO::class.java)
        } catch (e: JsonSyntaxException) { }

        val token = responseString.trim()
        if (token.length > 5 && !token.contains(" ") && !token.contains("{")) {
            val dummyUserDto = com.example.tripalert.data.remote.dto.UserResponseDTO(
                id = -1,
                username = UNKNOWN_USER_PLACEHOLDER,
                timeOffset = 0,
                preferredTransport = com.example.tripalert.domain.models.TransportType.WALK
            )
            return AuthResponseDTO(token = token, user = dummyUserDto)
        }
        throw Exception("Invalid server response: $responseString")
    }

    private suspend fun setSession(token: String, userDto: com.example.tripalert.data.remote.dto.UserResponseDTO) {
        authToken = token
        if (userDto.username == UNKNOWN_USER_PLACEHOLDER) {
            val idForFetch = currentUserId ?: throw IllegalStateException("User ID not set")
            _userFlow.value = null
            fetchProfileFromServer()
        } else {
            currentUserId = userDto.id
            _userFlow.value = UserMapper.fromDto(userDto, "password_hidden")
        }
    }

    override suspend fun signIn(username: String, password: String) {
        val request = LoginRequestDTO(username, password)
        try {
            val responseBody = api.login(request)
            val response = parseAuthResponse(responseBody)
            if (response.token.isNullOrBlank()) throw Exception("Empty token returned")

            currentUserId = response.user.id
            setSession(response.token, response.user)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun createUser(user: User, pass: String) {
        val dto = CreateUserDTO(
            username = user.username,
            password = pass,
            timeOffset = user.timeOffset,
            preferredTransport = user.preferredTransport
        )
        try {
            val responseBody = api.createUser(dto)
            val response = parseAuthResponse(responseBody)

            if (!response.token.isNullOrBlank()) {
                currentUserId = response.user.id
                setSession(response.token, response.user)
            }
        } catch (e: Exception) {
        }
    }

    override suspend fun fetchProfileFromServer() {
        val id = currentUserId ?: throw IllegalStateException("No user ID set")
        try {
            val response = api.getUserById(id)
            _userFlow.value = UserMapper.fromDto(response, "password_hidden")
        } catch (e: Exception) {
            if (e is HttpException && (e.code() == 401 || e.code() == 403)) {
                signOut()
            }
            throw e
        }
    }

    override suspend fun updateProfile(user: User) {
        val id = currentUserId ?: throw IllegalStateException("No user ID set")
        val updateDto = UpdateUserDTO(user.username, user.timeOffset, user.preferredTransport)
        val response = api.updateUser(id, updateDto)
        _userFlow.value = UserMapper.fromDto(response, "hidden")
    }

    override suspend fun deleteUser() {
        val id = currentUserId ?: throw IllegalStateException("No user ID set")
        api.deleteUser(id)
        signOut()
    }

    override suspend fun signOut() {
        authToken = null
        currentUserId = null
        _userFlow.value = null
    }
}

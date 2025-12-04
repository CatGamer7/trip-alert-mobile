package com.example.tripalert.ui.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripalert.domain.models.TransportType
import com.example.tripalert.domain.models.User
import com.example.tripalert.domain.repository.UserRepository
import com.example.tripalert.domain.usecase.user.CreateUserUseCase
import com.example.tripalert.domain.usecase.user.DeleteUserUseCase
import com.example.tripalert.domain.usecase.user.GetUserProfileUseCase
import com.example.tripalert.domain.usecase.user.SignInUseCase
import com.example.tripalert.domain.usecase.user.SignOutUseCase
import com.example.tripalert.domain.usecase.user.UpdateProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class UserViewModel(
    private val signInUseCase: SignInUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    private val _serverResponse = MutableStateFlow("")
    val serverResponse: StateFlow<String> = _serverResponse

    private val _currentUserState = MutableStateFlow<User?>(null)
    val currentUserState: StateFlow<User?> = _currentUserState

    init {
        viewModelScope.launch {
            getUserProfileUseCase().collectLatest { user ->
                _currentUserState.value = user
            }
        }
    }

    fun signIn(username: String, pass: String) {
        viewModelScope.launch {
            try {
                _serverResponse.value = "Logging in..."
                signInUseCase(username, pass)
                _serverResponse.value = "Success! Logged in as $username"
            } catch (e: Exception) {
                _serverResponse.value = "Login Error: ${e.message}"
            }
        }
    }

    fun createUser(username: String, pass: String, offset: Int, transport: TransportType) {
        viewModelScope.launch {
            try {
                _serverResponse.value = "Creating user..."
                createUserUseCase(username, pass, offset, transport)
                _serverResponse.value = "User created! Now please Sign In."
            } catch (e: Exception) {
                _serverResponse.value = "Create Error: ${e.message}"
            }
        }
    }

    fun updateUser(offset: Int?, transport: TransportType?) {
        viewModelScope.launch {
            try {
                _serverResponse.value = "Updating..."
                val userUpdates = User(
                    username = _currentUserState.value?.username ?: throw IllegalStateException("User not logged in."),
                    timeOffset = offset ?: _currentUserState.value!!.timeOffset,
                    preferredTransport = transport ?: _currentUserState.value!!.preferredTransport
                )

                updateProfileUseCase(userUpdates)
                _serverResponse.value = "Update successful"
            } catch (e: Exception) {
                _serverResponse.value = "Update Error: ${e.message}"
            }
        }
    }

    fun fetchProfile() {
        _serverResponse.value = "Profile is already fetched on login or automatically updated by Flow."
    }

    fun deleteUser() {
        viewModelScope.launch {
            try {
                _serverResponse.value = "Deleting user..."
                deleteUserUseCase()
                _serverResponse.value = "User deleted. Logged out."
            } catch (e: Exception) {
                _serverResponse.value = "Delete Error: ${e.message}"
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                _serverResponse.value = "Signing out..."
                signOutUseCase()
                _serverResponse.value = "Signed out."
            } catch (e: Exception) {
                _serverResponse.value = "Sign out Error: ${e.message}"
            }
        }
    }
}
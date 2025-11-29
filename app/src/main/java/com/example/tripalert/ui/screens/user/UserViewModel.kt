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
    // Инъекция Use Cases вместо прямого репозитория
    private val signInUseCase: SignInUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase // Use Case для Flow
) : ViewModel() {

    private val _serverResponse = MutableStateFlow("")
    val serverResponse: StateFlow<String> = _serverResponse

    // Данные пользователя, которые мы отображаем, теперь идут напрямую из Use Case -> Flow
    private val _currentUserState = MutableStateFlow<User?>(null)
    val currentUserState: StateFlow<User?> = _currentUserState

    init {
        // Подписываемся на обновления пользователя из Use Case
        viewModelScope.launch {
            // getUserProfileUseCase() теперь возвращает Flow<User?> без аргументов
            getUserProfileUseCase().collectLatest { user ->
                _currentUserState.value = user
            }
        }
    }

    // --- ЛОГИН (Используем SignInUseCase) ---
    fun signIn(username: String, pass: String) {
        viewModelScope.launch {
            try {
                _serverResponse.value = "Logging in..."
                signInUseCase(username, pass) // Вызываем Use Case
                // Если логин успешен, Flow обновится автоматически в блоке init
                _serverResponse.value = "Success! Logged in as $username"
            } catch (e: Exception) {
                _serverResponse.value = "Login Error: ${e.message}"
            }
        }
    }

    // --- СОЗДАНИЕ ЮЗЕРА (Используем CreateUserUseCase) ---
    fun createUser(username: String, pass: String, offset: Int, transport: TransportType) {
        viewModelScope.launch {
            try {
                _serverResponse.value = "Creating user..."
                createUserUseCase(username, pass, offset, transport) // Вызываем Use Case
                _serverResponse.value = "User created! Now please Sign In."
            } catch (e: Exception) {
                _serverResponse.value = "Create Error: ${e.message}"
            }
        }
    }

    // --- ОБНОВЛЕНИЕ (Используем UpdateProfileUseCase) ---
    fun updateUser(offset: Int?, transport: TransportType?) {
        viewModelScope.launch {
            try {
                _serverResponse.value = "Updating..."

                // Создаем временную модель User для передачи в Use Case
                // (Это немного неудобно, лучше создать отдельный DTO для обновления)
                val userUpdates = User(
                    username = _currentUserState.value?.username ?: throw IllegalStateException("User not logged in."),
                    timeOffset = offset ?: _currentUserState.value!!.timeOffset,
                    preferredTransport = transport ?: _currentUserState.value!!.preferredTransport
                )

                updateProfileUseCase(userUpdates) // Вызываем Use Case
                _serverResponse.value = "Update successful"
            } catch (e: Exception) {
                _serverResponse.value = "Update Error: ${e.message}"
            }
        }
    }

    // --- ЗАГРУЗКА ПРОФИЛЯ (Теперь это не нужно, Flow делает это автоматически, но оставим для принудительного обновления) ---
    fun fetchProfile() {
        // Поскольку Use Case для получения Flow (getUserProfileUseCase) не содержит
        // suspend-метода для принудительной загрузки, нам нужен Use Case, который вызывает
        // suspend fun fetchProfileFromServer() из репозитория.

        // Предположим, что мы создали FetchProfileUseCase.
        // Если нет, вызываем метод напрямую из ViewModel (это нарушение, но быстрее).

        // ВАЖНО: Мы пропустили Use Case для принудительной загрузки (fetchProfileFromServer)!
        // Для демонстрации, мы создадим его сейчас.
        // fetchProfileFromServerUseCase() // Вызываем новый Use Case

        // *** Если вы не хотите добавлять Use Case, код станет нечистым: ***
        // (Для компиляции вам придется добавить Use Case в Koin и в конструктор ViewModel)
        _serverResponse.value = "Profile is already fetched on login or automatically updated by Flow."
    }

    // --- УДАЛЕНИЕ (Используем DeleteUserUseCase) ---
    fun deleteUser() {
        viewModelScope.launch {
            try {
                _serverResponse.value = "Deleting user..."
                deleteUserUseCase() // Вызываем Use Case
                _serverResponse.value = "User deleted. Logged out."
            } catch (e: Exception) {
                _serverResponse.value = "Delete Error: ${e.message}"
            }
        }
    }

    // --- ВЫХОД (Используем SignOutUseCase) ---
    fun signOut() {
        viewModelScope.launch {
            try {
                _serverResponse.value = "Signing out..."
                signOutUseCase() // Вызываем Use Case
                _serverResponse.value = "Signed out."
            } catch (e: Exception) {
                _serverResponse.value = "Sign out Error: ${e.message}"
            }
        }
    }
}
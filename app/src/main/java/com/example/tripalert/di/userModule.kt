package com.example.tripalert.di

import com.example.tripalert.data.remote.api.UserApi
import com.example.tripalert.data.repository.UserRepositoryImpl
import com.example.tripalert.domain.repository.UserRepository
import com.example.tripalert.domain.usecase.user.*
import com.example.tripalert.ui.screens.user.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module
import retrofit2.Retrofit

val userModule = module {

    single { get<Retrofit>().create(UserApi::class.java) }
    single<UserRepository> { UserRepositoryImpl(api = get(), gson = get()) }

    factory { SignInUseCase(get()) }
    factory { SignOutUseCase(get()) }
    factory { GetUserProfileUseCase(get()) }
    factory { CreateUserUseCase(get()) }
    factory { UpdateProfileUseCase(get()) }
    factory { DeleteUserUseCase(get()) }

    viewModel {
        UserViewModel(
            signInUseCase = get(),
            signOutUseCase = get(),
            createUserUseCase = get(),
            updateProfileUseCase = get(),
            deleteUserUseCase = get(),
            getUserProfileUseCase = get()
        )
    }
}
package com.example.tripalert.di

import com.example.tripalert.data.remote.api.UserApi
import com.example.tripalert.ui.screens.user.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val userModule = module {

    // API
    single { get<Retrofit>().create(UserApi::class.java) }

    // ViewModel
    viewModel { UserViewModel(api = get()) }
}

package com.example.tripalert.di

import com.example.tripalert.data.mapper.TripMapper
import com.example.tripalert.data.mapper.UserMapper
import com.example.tripalert.data.remote.api.TripApi
import com.example.tripalert.data.repository.TripRepositoryImpl
import com.example.tripalert.domain.repository.TripRepository
import com.example.tripalert.domain.usecase.trip.GetTripsUseCase
import com.example.tripalert.ui.screens.tripdetails.TripDetailsViewModel
import com.example.tripalert.ui.screens.triplist.TripListViewModel
import com.example.tripalert.util.LocalDateTimeAdapter
import com.google.gson.GsonBuilder
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

const val BASE_URL = "http://192.168.1.34:8080/api/"

val appModule = module {

    // Gson
    single {
        GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .create()
    }

    // Retrofit
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    // API
    single { get<Retrofit>().create(TripApi::class.java) }

    // Репозитории
    single<TripRepository> { TripRepositoryImpl(api = get()) }

    // UseCases
    single { GetTripsUseCase(get()) }

    // ViewModels
    viewModel { TripListViewModel(get()) }

    viewModel { parameters ->
        TripDetailsViewModel(
            tripRepository = get(),
            savedStateHandle = parameters.get()
        )
    }

    // Мапперы
    single { TripMapper }
    single { UserMapper }
}

package com.example.tripalert.di

import com.example.tripalert.data.mapper.TripMapper
import com.example.tripalert.data.mapper.UserMapper
import com.example.tripalert.data.remote.api.TripApi
import com.example.tripalert.data.repository.TripRepositoryImpl
import com.example.tripalert.domain.repository.TripRepository
import com.example.tripalert.domain.usecase.trip.GetTripsUseCase
import com.example.tripalert.ui.screens.tripdetails.TripDetailsViewModel
import com.example.tripalert.ui.screens.triplist.TripListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit



val appModule = module {


    single { get<Retrofit>().create(TripApi::class.java) }


    single<TripRepository> { TripRepositoryImpl(api = get()) }

    // UseCases
    single { GetTripsUseCase(get()) }

    // ViewModels
    viewModel { TripListViewModel(get()) }


    viewModel {
        TripDetailsViewModel(
            tripRepository = get(),
            savedStateHandle = get()
        )
    }

    single { TripMapper }
    single { UserMapper }
}
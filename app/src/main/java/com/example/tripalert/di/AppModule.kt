package com.example.tripalert.di

import org.koin.androidx.viewmodel.dsl.viewModel
import com.example.tripalert.data.mapper.TripMapper
import com.example.tripalert.data.mapper.UserMapper
import com.example.tripalert.data.mapper.ReminderMapper
import com.example.tripalert.data.remote.api.ReminderApi
import com.example.tripalert.data.remote.api.RetrofitInstance
import com.example.tripalert.data.remote.api.TripApi
import com.example.tripalert.data.remote.api.UserApi
import com.example.tripalert.data.repository.*
import com.example.tripalert.domain.repository.ReminderRepository
import com.example.tripalert.domain.repository.TripRepository
import com.example.tripalert.domain.repository.UserRepository
import com.example.tripalert.domain.usecase.*
import com.example.tripalert.domain.usecase.reminder.CreateReminderUseCase
import com.example.tripalert.domain.usecase.reminder.DeleteReminderUseCase
import com.example.tripalert.domain.usecase.reminder.GetRemindersForTripUseCase
import com.example.tripalert.domain.usecase.reminder.UpdateReminderUseCase
import com.example.tripalert.domain.usecase.trip.CreateTripUseCase
import com.example.tripalert.domain.usecase.trip.DeleteTripUseCase
import com.example.tripalert.domain.usecase.trip.GetTripByIdUseCase
import com.example.tripalert.domain.usecase.trip.GetTripsUseCase
import com.example.tripalert.domain.usecase.trip.UpdateTripUseCase
import com.example.tripalert.domain.usecase.user.GetUserProfileUseCase
import com.example.tripalert.domain.usecase.user.SignInAnonymouslyUseCase
import com.example.tripalert.domain.usecase.user.SignOutUseCase
import com.example.tripalert.domain.usecase.user.UpdateProfileUseCase
import com.example.tripalert.ui.screens.tripdetails.TripDetailsViewModel
import org.koin.dsl.module

val appModule = module {

    // --- Mappers ---
    single { TripMapper }
    single { ReminderMapper }
    single { UserMapper }

    // --- Retrofit API ---
    single<TripApi> { RetrofitInstance.tripApi }
    single<ReminderApi> { RetrofitInstance.reminderApi }
    single<UserApi> { RetrofitInstance.userApi }

    // --- Repositories ---
    single<TripRepository> { TripRepositoryImpl(get(), get()) }
    single<ReminderRepository> { ReminderRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }

    // --- UseCases ---
    single {
        TripUseCases(
            createTrip = CreateTripUseCase(get()),
            updateTrip = UpdateTripUseCase(get()),
            getTripById = GetTripByIdUseCase(get()),
            getTrips = GetTripsUseCase(get()),
            deleteTrip = DeleteTripUseCase(get())
        )
    }

    single {
        ReminderUseCases(
            createReminder = CreateReminderUseCase(get()),
            updateReminder = UpdateReminderUseCase(get()),
            deleteReminder = DeleteReminderUseCase(get()),
            getRemindersForTrip = GetRemindersForTripUseCase(get())
        )
    }

    single {
        UserUseCases(
            getUserProfile = GetUserProfileUseCase(get()),
            updateProfile = UpdateProfileUseCase(get()),
            signOut = SignOutUseCase(get()),
            signInAnonymously = SignInAnonymouslyUseCase(get())
        )
    }

    // --- ViewModels ---
    viewModel { TripDetailsViewModel(get(), get()) }
}

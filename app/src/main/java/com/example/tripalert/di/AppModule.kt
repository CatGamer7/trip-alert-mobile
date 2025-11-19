package com.example.tripalert.di

import androidx.room.Room
import com.example.tripalert.data.local.TripDatabase
import com.example.tripalert.data.local.dao.TripDao
import com.example.tripalert.data.mapper.TripMapper
import com.example.tripalert.data.mapper.UserMapper
import com.example.tripalert.data.remote.api.TripApi
import com.example.tripalert.data.repository.TripRepositoryImpl
import com.example.tripalert.data.service.GeocodingServiceImpl
import com.example.tripalert.domain.repository.TripRepository
import com.example.tripalert.domain.service.GeocodingService
import com.example.tripalert.ui.screens.tripdetails.TripDetailsViewModel
import com.example.tripalert.ui.screens.triplist.TripListViewModel
import com.example.tripalert.util.LocalDateTimeAdapter
import com.google.gson.GsonBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

const val BASE_URL = "http://10.0.2.2:8080/api/"

val appModule = module {

    // --- 1. NETWORK ---
    single {
        GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .create()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    single { get<Retrofit>().create(TripApi::class.java) }

    // --- 2. DATABASE (ROOM) ---

    single {
        Room.databaseBuilder(
            androidContext(),
            TripDatabase::class.java,
            "trip_alert_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single<TripDao> {
        get<TripDatabase>().tripDao()
    }

    // --- 3. UTILS ---
    single { TripMapper }
    single { UserMapper }

    single<GeocodingService> {
        GeocodingServiceImpl()
    }

    // --- 4. REPOSITORY ---
    single<TripRepository> {
        TripRepositoryImpl(
            api = get(),
            dao = get(), // Теперь Koin знает, как получить правильный DAO
            mapper = get(),
            geocodingService = get()
        )
    }

    // --- 5. VIEW MODELS ---
    viewModel {
        TripListViewModel(
            tripRepository = get()
        )
    }

    viewModel { parameters ->
        TripDetailsViewModel(
            tripRepository = get(),
            savedStateHandle = parameters.get()
        )
    }
}
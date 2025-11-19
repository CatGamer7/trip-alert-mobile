package com.example.tripalert.data.remote.api

import com.example.tripalert.data.remote.dto.CoordinateDTO
import com.example.tripalert.data.remote.gson.GeoJsonPointAdapter
import com.example.tripalert.util.LocalDateTimeAdapter
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    // Gson с адаптерами для LocalDateTime и CoordinateDTO (GeoJSON)
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .registerTypeAdapter(CoordinateDTO::class.java, GeoJsonPointAdapter())
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val tripApi: TripApi by lazy { retrofit.create(TripApi::class.java) }
    val reminderApi: ReminderApi by lazy { retrofit.create(ReminderApi::class.java) }
    val userApi: UserApi by lazy { retrofit.create(UserApi::class.java) }
}

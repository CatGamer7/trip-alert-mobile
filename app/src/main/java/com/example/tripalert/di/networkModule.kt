package com.example.tripalert.di

import com.example.tripalert.util.GeoJsonPointAdapter
import com.example.tripalert.util.LocalDateTimeAdapter
import com.example.tripalert.data.remote.dto.CoordinateDTO
import com.example.tripalert.domain.repository.UserRepository
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

// Базовый URL
private const val BASE_URL = "http://10.0.2.2:8080/"

val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}
class AuthInterceptor(private val userRepositoryLazy: Lazy<UserRepository>) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = userRepositoryLazy.value.getCurrentAuthToken()

        val newRequestBuilder = request.newBuilder()
            .header("Connection", "close")
            .header("Cache-Control", "no-cache")

        if (token != null) {
            newRequestBuilder.header("Authorization", token)
        }

        return chain.proceed(newRequestBuilder.build())
    }
}

val networkModule = module {


    single { LocalDateTimeAdapter() }
    single { GeoJsonPointAdapter() }

    single {
        GsonBuilder()

            .registerTypeAdapter(LocalDateTime::class.java, get<LocalDateTimeAdapter>())
            .registerTypeAdapter(CoordinateDTO::class.java, get<GeoJsonPointAdapter>())
            .create()
    }



    single {
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }


    single {
        AuthInterceptor(lazy { get<UserRepository>() })
    }


    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(get<AuthInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }


    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }
}
package com.example.tripalert.di

import com.example.tripalert.data.remote.api.UserApi
import com.example.tripalert.domain.repository.UserRepository
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import java.time.LocalDateTime
import com.example.tripalert.data.remote.dto.CoordinateDTO
import com.example.tripalert.util.GeoJsonPointAdapter
import com.example.tripalert.util.LocalDateTimeAdapter
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://10.0.2.2:8080/"

class AuthInterceptor(private val userRepositoryLazy: Lazy<UserRepository>) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val rawToken = userRepositoryLazy.value.getCurrentAuthToken()

        val newRequestBuilder = request.newBuilder()
            .header("Connection", "close")
            .header("Cache-Control", "no-cache")

        if (rawToken != null) {
            newRequestBuilder.header("Authorization", "Bearer $rawToken")
        }

        return chain.proceed(newRequestBuilder.build())
    }
}

val networkModule = module {

    single { LocalDateTimeAdapter() }
    single { GeoJsonPointAdapter() }

    single {
        GsonBuilder()
            .setLenient()
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

    single {
        get<Retrofit>().create(UserApi::class.java)
    }
}
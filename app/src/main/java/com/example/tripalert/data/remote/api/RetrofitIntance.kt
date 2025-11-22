package com.example.tripalert.data.remote.api

import com.example.tripalert.data.remote.dto.CoordinateDTO
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.concurrent.TimeUnit



class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun serialize(
        src: LocalDateTime,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(src.format(formatter))
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): LocalDateTime {
        val dateTimeString = json.asString
        return try {
            LocalDateTime.parse(dateTimeString, formatter)
        } catch (e: DateTimeParseException) {

            LocalDateTime.parse(dateTimeString)
        }
    }
}


class GeoJsonPointAdapter : JsonSerializer<CoordinateDTO>, JsonDeserializer<CoordinateDTO> {


    private val listType: Type = object : TypeToken<List<Double>>() {}.type

    override fun serialize(
        src: CoordinateDTO,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {

        val coordinates = listOf(src.x, src.y)
        return context.serialize(coordinates, listType)
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): CoordinateDTO {


        val coordinatesArray = if (json.isJsonObject) {

            json.asJsonObject.getAsJsonArray("coordinates")
        } else {

            json.asJsonArray
        }

        val lng = coordinatesArray[0].asDouble
        val lat = coordinatesArray[1].asDouble

        return CoordinateDTO(x = lng, y = lat)
    }
}




object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .registerTypeAdapter(CoordinateDTO::class.java, GeoJsonPointAdapter()) // Важно для GeoJSON
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


}
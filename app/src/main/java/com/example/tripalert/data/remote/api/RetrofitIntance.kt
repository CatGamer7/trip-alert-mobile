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

// --- НЕОБХОДИМЫЕ АДАПТЕРЫ ДЛЯ GSON ---

/**
 * Адаптер для корректного парсинга java.time.LocalDateTime.
 * Поддерживает стандарт ISO 8601 (например, "2025-11-20T08:30:00").
 */
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
            // Попытка парсинга без форматтера (для обработки UTC Z-suffix, если он есть)
            LocalDateTime.parse(dateTimeString)
        }
    }
}

/**
 * Адаптер для парсинга CoordinateDTO из формата GeoJSON Point (массив [longitude, latitude]).
 *
 * Ожидается: {"type": "Point", "coordinates": [longitude, latitude]}
 * Сериализуется: [longitude, latitude]
 */
class GeoJsonPointAdapter : JsonSerializer<CoordinateDTO>, JsonDeserializer<CoordinateDTO> {

    // Тип для List<Double> (используется для списка координат)
    private val listType: Type = object : TypeToken<List<Double>>() {}.type

    override fun serialize(
        src: CoordinateDTO,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        // При отправке данных на сервер мы сериализуем только массив координат: [x (lng), y (lat)]
        val coordinates = listOf(src.x, src.y)
        return context.serialize(coordinates, listType)
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): CoordinateDTO {
        // При получении данных от сервера мы ожидаем формат GeoJSON Point:
        // {"type": "Point", "coordinates": [longitude, latitude]}

        val coordinatesArray = if (json.isJsonObject) {
            // Если приходит объект (полный GeoJSON Point)
            json.asJsonObject.getAsJsonArray("coordinates")
        } else {
            // Если приходит просто массив (иногда используется для вложенных полей)
            json.asJsonArray
        }

        val lng = coordinatesArray[0].asDouble
        val lat = coordinatesArray[1].asDouble

        return CoordinateDTO(x = lng, y = lat)
    }
}


// --- САМ КЛАСС RETROFIT INSTANCE ---

object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    // Gson с адаптерами для LocalDateTime и CoordinateDTO (GeoJSON)
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .registerTypeAdapter(CoordinateDTO::class.java, GeoJsonPointAdapter()) // Важно для GeoJSON
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        // Уровень BODY позволяет видеть полные заголовки и тело запроса/ответа в логах Logcat
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            // Таймауты увеличены для надежности при отладке/медленной сети
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            // Используем Gson, настроенный с кастомными адаптерами
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // Лень-инициализация API-интерфейсов
    // Вам необходимо создать эти интерфейсы: TripApi, ReminderApi, UserApi
    // Пример: interface TripApi { ... }
    // val tripApi: TripApi by lazy { retrofit.create(TripApi::class.java) }
    // val reminderApi: ReminderApi by lazy { retrofit.create(ReminderApi::class.java) }
    // val userApi: UserApi by lazy { retrofit.create(UserApi::class.java) }
}
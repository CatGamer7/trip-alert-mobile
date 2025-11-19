package com.example.tripalert.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Адаптер Gson для сериализации и десериализации объектов LocalDateTime
 * в формат строки ISO 8601 и обратно.
 * * Реализует JsonSerializer и JsonDeserializer.
 * ВАЖНО: Классы com.google.gson.* используются вместо com.squareup.moshi.*.
 */
class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    // Форматтер для стандартного формата ISO 8601 (например, "2023-11-19T10:30:00").
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    /**
     * Сериализация (LocalDateTime -> JSON String)
     * Преобразует объект LocalDateTime в строковый примитив JSON.
     */
    override fun serialize(
        src: LocalDateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        // Если объект null, возвращаем пустой примитив.
        return if (src == null) {
            JsonPrimitive("")
        } else {
            // Форматируем дату/время в строку и оборачиваем в JsonPrimitive.
            JsonPrimitive(src.format(formatter))
        }
    }

    /**
     * Десериализация (JSON String -> LocalDateTime)
     * Преобразует строковый элемент JSON в объект LocalDateTime.
     */
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime? {
        // Проверяем на null, пустую строку или не-строковый формат.
        if (json == null || json.isJsonNull || !json.isJsonPrimitive) {
            return null
        }

        val dateTimeString = json.asString.trim()
        if (dateTimeString.isBlank()) {
            return null
        }

        return try {
            // Используем стандартный ISO форматтер для парсинга.
            LocalDateTime.parse(dateTimeString, formatter)
        } catch (e: DateTimeParseException) {
            // Если парсинг не удался, выбрасываем ошибку Gson для правильной обработки.
            throw JsonParseException("Не удалось распарсить LocalDateTime: '$dateTimeString'", e)
        }
    }
}
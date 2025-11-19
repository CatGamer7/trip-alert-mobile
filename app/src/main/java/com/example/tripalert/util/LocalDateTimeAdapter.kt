package com.example.tripalert.util

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Адаптер для Gson, позволяющий корректно сериализовать/десериализовать
 * объекты LocalDateTime в строки формата ISO-8601 (например, "2025-11-05T12:30:00").
 */
class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    // Форматтер для ISO-8601, который мы используем
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    /**
     * Сериализация (LocalDateTime -> JSON String)
     */
    override fun serialize(
        src: LocalDateTime,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        // Преобразуем объект LocalDateTime в строку
        return JsonPrimitive(formatter.format(src))
    }

    /**
     * Десериализация (JSON String -> LocalDateTime)
     */
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): LocalDateTime {
        try {
            // Парсим строку JSON обратно в объект LocalDateTime
            return LocalDateTime.parse(json.asString, formatter)
        } catch (e: Exception) {
            // Логируем ошибку, если формат строки не соответствует ISO_LOCAL_DATE_TIME
            throw JsonParseException("Не удалось распарсить LocalDateTime: ${json.asString}", e)
        }
    }
}
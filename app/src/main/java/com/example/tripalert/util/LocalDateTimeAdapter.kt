package com.example.tripalert.util

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class LocalDateTimeAdapter : JsonSerializer<LocalDateTime?>, JsonDeserializer<LocalDateTime?> {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun serialize(
        src: LocalDateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        if (src == null) {
            return null
        }
        return JsonPrimitive(src.format(formatter))
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime? {
        if (json == null || json.isJsonNull || !json.isJsonPrimitive) {
            return null
        }

        val dateTimeString = json.asString.trim()
        if (dateTimeString.isBlank()) {
            return null
        }

        return try {
            LocalDateTime.parse(dateTimeString, formatter)
        } catch (e: DateTimeParseException) {
            throw JsonParseException("Failed to parse LocalDateTime: '$dateTimeString'", e)
        }
    }
}
package com.example.tripalert.data.remote.gson

import com.example.tripalert.domain.models.GeoPoint // Используем Domain-объект
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException

/**
 * Адаптер для чтения/записи GeoJSON Point { "type": "Point", "coordinates": [lon, lat] }
 * Напрямую маппит в Domain-объект GeoPoint.
 */
class GeoJsonPointAdapter : TypeAdapter<GeoPoint>() { // Привязываемся к GeoPoint

    @Throws(IOException::class)
    override fun read(reader: JsonReader): GeoPoint {
        var lon = 0.0
        var lat = 0.0
        var foundCoordinates = false

        // Проверка на null (если сервер прислал null)
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            // Возвращаем пустой GeoPoint или выбрасываем исключение, если он обязателен.
            // Предположим, что он не может быть null.
            return GeoPoint(0.0, 0.0)
        }

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "type" -> {
                    // Читаем, но игнорируем
                    if (reader.peek() != JsonToken.NULL) reader.nextString() else reader.nextNull()
                }
                "coordinates" -> {
                    foundCoordinates = true
                    reader.beginArray()
                    // GeoJSON порядок: [lon (x), lat (y)]
                    if (reader.hasNext()) lon = reader.nextDouble()
                    if (reader.hasNext()) lat = reader.nextDouble()
                    // Пропускаем возможные z-координаты и др.
                    while (reader.hasNext()) reader.skipValue()
                    reader.endArray()
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        // Проверка: мы должны были найти координаты
        if (!foundCoordinates) {
            throw IOException("GeoJSON object missing 'coordinates' array.")
        }

        // В Domain-модели: GeoPoint(latitude, longitude)
        return GeoPoint(latitude = lat, longitude = lon)
    }

    @Throws(IOException::class)
    override fun write(writer: JsonWriter, value: GeoPoint) {
        // Мы всегда отправляем корректный GeoJSON
        writer.beginObject()
        writer.name("type").value("Point")
        writer.name("coordinates")
        writer.beginArray()
        // GeoJSON: [lon (x), lat (y)]
        writer.value(value.longitude)
        writer.value(value.latitude)
        writer.endArray()
        writer.endObject()
    }
}
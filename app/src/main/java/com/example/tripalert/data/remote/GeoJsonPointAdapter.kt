package com.example.tripalert.data.remote.gson

import com.example.tripalert.data.remote.dto.CoordinateDTO
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

/**
 * Читает/пишет GeoJSON Point:
 * Read example:
 *  { "type": "Point", "coordinates": [lon, lat] }
 *
 * Write (we также отправляем GeoJSON Point на сервер):
 *  { "type": "Point", "coordinates": [lon, lat] }
 *
 * Internally maps to CoordinateDTO(x = lon, y = lat)
 */
class GeoJsonPointAdapter : TypeAdapter<CoordinateDTO>() {

    override fun read(reader: JsonReader): CoordinateDTO {
        var lon = 0.0
        var lat = 0.0

        reader.beginObject()
        while (reader.hasNext()) {
            val name = reader.nextName()
            when (name) {
                "type" -> {
                    // читаем и игнорируем (ожидаем "Point")
                    if (reader.peek() != JsonToken.NULL) reader.nextString() else reader.nextNull()
                }
                "coordinates" -> {
                    reader.beginArray()
                    if (reader.hasNext()) lon = reader.nextDouble()
                    if (reader.hasNext()) lat = reader.nextDouble()
                    // пропускаем возможные дополнительные координаты
                    while (reader.hasNext()) reader.skipValue()
                    reader.endArray()
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return CoordinateDTO(x = lon, y = lat)
    }

    override fun write(writer: JsonWriter, value: CoordinateDTO) {
        // Записываем GeoJSON Point (server expects GeoJSON)
        writer.beginObject()
        writer.name("type").value("Point")
        writer.name("coordinates")
        writer.beginArray()
        // GeoJSON coordinates: [lon, lat]
        writer.value(value.x)
        writer.value(value.y)
        writer.endArray()
        writer.endObject()
    }
}

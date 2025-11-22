package com.example.tripalert.data.remote.gson

import com.example.tripalert.domain.models.GeoPoint
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException


class GeoJsonPointAdapter : TypeAdapter<GeoPoint>() {

    @Throws(IOException::class)
    override fun read(reader: JsonReader): GeoPoint {
        var lon = 0.0
        var lat = 0.0
        var foundCoordinates = false

        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return GeoPoint(0.0, 0.0)
        }

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "type" -> {
                    if (reader.peek() != JsonToken.NULL) reader.nextString() else reader.nextNull()
                }
                "coordinates" -> {
                    foundCoordinates = true
                    reader.beginArray()
                    if (reader.hasNext()) lon = reader.nextDouble()
                    if (reader.hasNext()) lat = reader.nextDouble()
                    while (reader.hasNext()) reader.skipValue()
                    reader.endArray()
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        if (!foundCoordinates) {
            throw IOException("GeoJSON object missing 'coordinates' array.")
        }

        return GeoPoint(latitude = lat, longitude = lon)
    }

    @Throws(IOException::class)
    override fun write(writer: JsonWriter, value: GeoPoint) {
        writer.beginObject()
        writer.name("type").value("Point")
        writer.name("coordinates")
        writer.beginArray()
        writer.value(value.longitude)
        writer.value(value.latitude)
        writer.endArray()
        writer.endObject()
    }
}
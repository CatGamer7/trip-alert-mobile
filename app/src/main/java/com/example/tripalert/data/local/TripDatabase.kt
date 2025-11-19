package com.example.tripalert.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tripalert.data.local.dao.TripDao
import com.example.tripalert.data.local.entity.TripEntity
import com.example.tripalert.data.local.converters.Converters // Создадим ниже

@Database(
    entities = [TripEntity::class], // Указываем нашу Entity
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class) // Нужно для LocalDateTime
abstract class TripDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
}
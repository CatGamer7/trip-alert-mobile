package com.example.tripalert.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.tripalert.data.local.entity.TripEntity

@Dao
interface TripDao {

    // --- ЧТЕНИЕ ---
    @Query("SELECT * FROM trips WHERE userId = :userId")
    suspend fun getTripsByUserId(userId: Long): List<TripEntity>

    @Query("SELECT * FROM trips WHERE id = :tripId")
    suspend fun getTripById(tripId: Long): TripEntity?

    // --- ВСТАВКА ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripEntity)

    // Вспомогательный метод для вставки списка (используется внутри транзакции)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(trips: List<TripEntity>)

    // --- УДАЛЕНИЕ ---
    @Query("DELETE FROM trips WHERE id = :tripId")
    suspend fun deleteTrip(tripId: Long)

    // Вспомогательный метод для очистки всей таблицы (используется внутри транзакции)
    @Query("DELETE FROM trips")
    suspend fun deleteAllTrips()

    // --- ОБНОВЛЕНИЕ ---
    @Update
    suspend fun updateTrip(trip: TripEntity)

    // --- ТРАНЗАКЦИИ (Сложная логика) ---

    /**
     * Удаляет все записи и вставляет новые.
     * Аннотация @Transaction гарантирует, что это произойдет атомарно (все или ничего).
     * Метод не абстрактный, так как у него есть тело.
     */
    @Transaction
    suspend fun clearAndInsertTrips(trips: List<TripEntity>) {
        deleteAllTrips() // Сначала чистим кэш
        insertAll(trips) // Потом записываем свежие данные
    }
}
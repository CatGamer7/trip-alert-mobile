package com.example.tripalert.util

// T — это тип данных, которые мы ожидаем получить (например, List<Trip>)
sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    // Успешный результат с данными
    class Success<T>(data: T) : Resource<T>(data)

    // Ошибка с сообщением и, возможно, старыми данными (для кэша)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    // Состояние загрузки (с данными или без)
    class Loading<T>(val isLoading: Boolean = true, data: T? = null) : Resource<T>(data)
}
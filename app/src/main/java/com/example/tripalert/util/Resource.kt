package com.example.tripalert.util

// T — это тип данных, которые мы ожидаем получить (например, List<Trip>)
sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    /** Успешное выполнение. */
    class Success<T>(data: T) : Resource<T>(data)

    /** Ошибка. */
    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)

    /** Состояние загрузки (если нужно). В этом приложении не используется, но полезно для полноты. */
    class Loading<T>(data: T? = null) : Resource<T>(data)

    /** Начальное или Неизвестное состояние (если нужно). */
    class Idle<T> : Resource<T>()
}
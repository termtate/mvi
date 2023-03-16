package com.example.myapplicationg.data.db

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateConverter {
    private val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @TypeConverter
    fun fromString(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, pattern) }
    }

    @TypeConverter
    fun dateToString(localDateTime: LocalDateTime?): String? {
        return localDateTime?.format(pattern)
    }
}
package com.example.myapplicationg.usecase

import org.junit.Test
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class FormatDateUseCase {
    operator fun invoke(localDateTime: LocalDateTime): String {
        val now = LocalDateTime.now()
        val duration = Duration.between(localDateTime, now)
        return if (duration.toDays() > 0) {
            "${duration.toDays()}天前"
        } else if (duration.toHours() > 0) {
            "${duration.toHours()}小时前"
        } else {
            "${duration.toMinutes()}分钟前"
        }
//        return localDateTime.toString()
    }
}

class TestDate {
    @Test
    fun test() {
        println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")))
    }
}
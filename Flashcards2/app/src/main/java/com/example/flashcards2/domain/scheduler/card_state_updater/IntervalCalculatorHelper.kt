package com.example.flashcards2.domain.scheduler.card_state_updater

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

class IntervalCalculatorHelper {

     fun next_day_at_secs_since_now(): Long{

        val now = LocalDateTime.now()
        val endOfTheDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59))

        val duration = Duration.between(now, endOfTheDay)

        return duration.seconds
    }

   fun next_day_at(): Long{

        val endOfTheDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59))

        return endOfTheDay.toEpochSecond(ZoneOffset.UTC)
    }


    fun nowTimestampSecond(): Long {
        return LocalDateTime.now()
            .atZone(ZoneId.systemDefault())
            .toEpochSecond()
    }

    fun days_elapsed(timeCreationSeconds: Long): Long{
       val crtDateTime = Instant.ofEpochSecond(timeCreationSeconds)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        val now: LocalDateTime = LocalDateTime.now()

        return Duration.between(crtDateTime, now).toDays()
    }
}
package com.example.flashcards2.domain.scheduler.timestamp

import com.example.flashcards2.domain.scheduler.card_state_updater.Constants.PYTHON_UNIT_TESTS
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class TimestampSecs(var value: Long = 0) {

    fun now(): TimestampSecs {

        this.value =  System.currentTimeMillis() / 1000
        return this

    }

    fun adding_secs(secs: Long): TimestampSecs {
        return TimestampSecs(value + secs)
    }

    fun local_datetime(): Result<LocalDateTime> {
        return try {
            val dateTime = Instant.ofEpochSecond(value)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
            Result.success(dateTime)
        } catch (e: Exception) {
            Result.failure(IllegalArgumentException("invalid timestamp", e))
        }
    }

    fun elapsed(): Duration {
        return if (PYTHON_UNIT_TESTS) {
            // shift clock around rollover time to accommodate Python tests that make bad
            // assumptions. we should update the tests in the future and remove this
            // hack.
            var elap = Duration.between(Instant.EPOCH, Instant.now())
            val now = LocalDateTime.now()
            if (now.hour in 2..3) {
                elap = elap.minus(Duration.ofHours(2))
            }
            elap
        } else {
            Duration.between(Instant.EPOCH, Instant.now())
        }
    }
}
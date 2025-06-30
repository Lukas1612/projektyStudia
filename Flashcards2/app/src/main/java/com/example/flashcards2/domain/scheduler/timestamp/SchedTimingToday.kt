package com.example.flashcards2.domain.scheduler.timestamp

class SchedTimingToday(
    val now: TimestampSecs,

    // The number of days that have passed since the collection was created.
    val days_elapsed: Long,

    // Timestamp of the next day rollover.
    val next_day_at: TimestampSecs
) {
}
package com.example.flashcards2.domain.scheduler.card_state_updater

sealed class IntervalKind {
    data class InSecs(val secs: Int): IntervalKind()
    data class InDays(val days: Int): IntervalKind()

    fun maybe_as_days(secs_until_rollover: Int): IntervalKind {
        return when(this){
            is InSecs -> {
                if (secs >= secs_until_rollover){
                    InDays(((secs - secs_until_rollover)/ 86400) + 1)

                }else{
                    this
                }
            }
            else -> {
                this
            }
        }
    }

    fun as_seconds(): Int{
        return when(this){
            is InSecs -> {
                 secs
            }

            is InDays -> {
                days.saturatingMultiply(86400)
            }
        }
    }

    fun Int.saturatingMultiply(other: Int): Int {
        val result = this.toLong() * other.toLong()
        return when {
            result > Int.MAX_VALUE -> Int.MAX_VALUE
            result < Int.MIN_VALUE -> Int.MIN_VALUE
            else -> result.toInt()
        }
    }
}
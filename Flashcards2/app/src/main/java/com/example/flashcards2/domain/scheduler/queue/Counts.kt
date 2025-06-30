package com.example.flashcards2.domain.scheduler.queue

class Counts(
    var new: Int,
    var learning: Int,
    var review: Int
) {

    fun isNonZero(): Boolean{
        return new > 0 || learning > 0 || review > 0
    }
}

package com.example.flashcards2.domain.scheduler.queue

enum class CardQueue {
    /// due is the order cards are shown in
    New,
    /// due is a unix timestamp
    Learn,
    /// due is days since creation date
    Review,
    DayLearn
}
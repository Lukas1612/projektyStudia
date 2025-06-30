package com.example.flashcards2.domain.scheduler.queue

import com.example.flashcards2.domain.scheduler.timestamp.TimestampSecs


class NewCard(
    val cardId: Int,
    val mtime: TimestampSecs,
    val template_index: Int,
    val hash: Long
) {
}
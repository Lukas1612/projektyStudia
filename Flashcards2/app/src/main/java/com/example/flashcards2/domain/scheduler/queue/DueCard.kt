package com.example.flashcards2.domain.scheduler.queue

import com.example.flashcards2.domain.scheduler.timestamp.TimestampSecs


class DueCard(
    val cardId: Int,
    val mtime: TimestampSecs,
    val due: Int,
    val kind: DueCardKind
) {
}
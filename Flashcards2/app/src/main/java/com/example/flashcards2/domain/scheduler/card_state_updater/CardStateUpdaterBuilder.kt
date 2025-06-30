package com.example.flashcards2.domain.scheduler.card_state_updater

import com.example.flashcards2.domain.model.Flashcard
import com.example.flashcards2.domain.scheduler.card_state_updater.Constants.PYTHON_UNIT_TESTS
import com.example.flashcards2.domain.scheduler.timestamp.SchedTimingToday
import com.example.flashcards2.domain.scheduler.timestamp.TimestampSecs


class CardStateUpdaterBuilder {
    fun create(flashcard: Flashcard, days_elapsed: Long): CardStateUpdater {
        val now = TimestampSecs().now()
        val timing = SchedTimingToday(
            now = now,
            days_elapsed = days_elapsed,
            next_day_at = TimestampSecs(IntervalCalculatorHelper().next_day_at())
        )

        val fuzz_seed = get_fuzz_seed(flashcard, false)

        return CardStateUpdater(
            flashcard = flashcard,
            now = now,
            timing = timing,
            fuzz_seed = fuzz_seed
        )
    }

    private fun get_fuzz_seed(flashcard: Flashcard, for_reschedule: Boolean): Long?{
        val reps = if (for_reschedule) {
            (flashcard.reps - 1).coerceAtLeast(0)
        } else {
            flashcard.reps
        }

        return get_fuzz_seed_for_id_and_reps(flashcard.id!!, reps)
    }

    private fun get_fuzz_seed_for_id_and_reps(card_id: Long, card_reps: Int): Long?{

        return if(PYTHON_UNIT_TESTS){
            null
        }else{
            card_id.plus(card_reps .toLong())
        }

    }
}
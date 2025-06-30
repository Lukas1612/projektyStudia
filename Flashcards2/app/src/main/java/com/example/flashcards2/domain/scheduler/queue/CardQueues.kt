package com.example.flashcards2.domain.scheduler.queue

import com.example.flashcards2.domain.model.Flashcard
import com.example.flashcards2.domain.scheduler.timestamp.SchedTimingToday
import com.example.flashcards2.domain.scheduler.timestamp.TimestampSecs



class CardQueues(
    var intraday_learning: MutableList<Flashcard> = mutableListOf(),
    var main: MutableList<Flashcard> = mutableListOf(),
    val counts: Counts,
    var current_learning_cutoff: TimestampSecs,
    val learn_ahead_secs: Long
) {
    //******** update queues **********
    fun update_queues_after_answering_card(
        card: Flashcard,
        timing: SchedTimingToday
    ) {
        pop_card(card.id!!)

        maybe_requeue_learning_card(card, timing)

        update_learning_cutoff_and_count()
    }

    /// Given the just-answered `card`, place it back in the learning queues if
    /// it's still due today. Avoid placing it in a position where it would
    /// be shown again immediately.
    fun maybe_requeue_learning_card(card: Flashcard, timing: SchedTimingToday) {
        if (!card.is_intraday_learning() || card.due >= timing.next_day_at.value) {
            //do nothing
        } else {
            requeue_learning_entry(card)
        }
    }

    fun requeue_learning_entry(card: Flashcard) {
        val cutoff = current_learn_ahead_cutoff()
        var updatedCard = card

        // If the provided entry would be shown again immediately, see if we
        // can place it after the next card instead
        if (card.due <= cutoff.value && learning_collapsed()) {
            val next = intraday_learning.firstOrNull()
            if (next != null && next.due >= card.due && (next.due + 1) < cutoff.value) {
                updatedCard.due = next.due + 1
            }
        }

        insert_intraday_learning_card(updatedCard)

    }

    private fun insert_intraday_learning_card(card: Flashcard) {


        if (card.due <= current_learn_ahead_cutoff().value) {
            counts.learning += 1
        }

        val targetIdx = intraday_learning.binarySearch { it.due.compareTo(card.due) }
            .let { if (it >= 0) it else -it - 1 }

        intraday_learning.add(targetIdx, card)

    }


    fun current_learn_ahead_cutoff(): TimestampSecs {
        return current_learning_cutoff.adding_secs(learn_ahead_secs)
    }

    fun learning_collapsed(): Boolean {
        return main.isEmpty()
    }

    private fun pop_card(cardId: Long): Flashcard {
        // This ignores the current cutoff, so may match if the provided
        // learning card is not yet due. It should not happen in normal
        // practice, but does happen in the Python unit tests, as they answer
        // learning cards early.

        val frontIntraday = intraday_learning.firstOrNull()
        if (frontIntraday?.id == cardId) {
            return intraday_learning.removeAt(0)
        }

        val frontMain = main.firstOrNull()
        if (frontMain?.id == cardId) {
            return main.removeAt(0)
        }

        throw IllegalArgumentException("not at top of queue")
    }

    fun update_learning_cutoff_and_count(){
        val last_ahead_cutoff = current_learn_ahead_cutoff()
        current_learning_cutoff = TimestampSecs().now()
        val new_ahead_cutoff = current_learn_ahead_cutoff()

        val new_learning_cards = intraday_learning
            .asSequence()
            .dropWhile { it.due <= last_ahead_cutoff.value }
            .takeWhile { it.due <= new_ahead_cutoff.value }
            .count()

        counts.learning += new_learning_cards
    }
    //********** get_next_card *******
    fun get_next_card(): Flashcard? {
        return get_queued_cards(false).getOrNull(0)
    }

    fun get_queued_cards(intraday_learning_only: Boolean): List<Flashcard>{

        if(intraday_learning_only){
            return intraday_now()
                .plus(intraday_ahead())
        }else{
            return intraday_now()
                .plus(main)
                .plus(intraday_ahead())
        }
    }

    fun intraday_now(): List<Flashcard>{

        val cutoff = current_learning_cutoff

        return intraday_learning
            .takeWhile { it.due <= cutoff.value }
    }

    fun intraday_ahead(): List<Flashcard>{
        val cutoff = current_learning_cutoff
        val aheadCutoff = current_learn_ahead_cutoff()

        return intraday_learning
            .dropWhile { it.due <= cutoff.value }
            .takeWhile { it.due <= aheadCutoff.value }
    }
}

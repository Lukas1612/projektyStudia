package com.example.flashcards2.domain.scheduler

import com.example.flashcards2.domain.model.Flashcard
import com.example.flashcards2.domain.scheduler.card_state_updater.CardStateUpdater
import com.example.flashcards2.domain.scheduler.card_state_updater.CardStateUpdaterBuilder
import com.example.flashcards2.domain.scheduler.card_state_updater.Constants.LEARN_AHEAD_SECS
import com.example.flashcards2.domain.scheduler.card_state_updater.IntervalCalculatorHelper
import com.example.flashcards2.domain.scheduler.card_state_updater.Rating
import com.example.flashcards2.domain.scheduler.card_state_updater.SchedulingStates
import com.example.flashcards2.domain.scheduler.queue.CardQueues
import com.example.flashcards2.domain.scheduler.queue.QueueBuilder
import com.example.flashcards2.domain.scheduler.states.CardState
import com.example.flashcards2.domain.scheduler.timestamp.SchedTimingToday
import com.example.flashcards2.domain.scheduler.timestamp.TimestampSecs
import com.example.flashcards2.domain.use_case.flashcard.FlashcardUseCases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Scheduler(
    val flashcardUseCases: FlashcardUseCases,
    val creationTimestampSeconds: Long,
    val groupId: Long
) {

    private var cardQueues: CardQueues? = null

    suspend fun get_queued_cards() = withContext(Dispatchers.IO) {

         QueueBuilder(flashcardUseCases).build(LEARN_AHEAD_SECS, creationTimestampSeconds, groupId).let { queues ->
             cardQueues = queues
        }
    }

    suspend fun get_next_card(): Flashcard? = withContext(Dispatchers.IO){

        clear_queues_if_day_changed()

        val queues = cardQueues ?: throw IllegalStateException("cardQueues is null")
        queues.get_next_card()
    }


    private suspend fun clear_queues_if_day_changed() = withContext(Dispatchers.IO) {
        val today_days_elapsed = IntervalCalculatorHelper().days_elapsed(creationTimestampSeconds)
        val day_rolled_over = is_stale(today_days_elapsed)

        if(day_rolled_over){
            get_queued_cards()
        }
    }

    private fun is_stale(current_day: Long): Boolean {
        return  IntervalCalculatorHelper().days_elapsed(creationTimestampSeconds) != current_day
    }


    fun update_queues_after_answering_card(card: Flashcard){

        val timing = SchedTimingToday(
            now = TimestampSecs(IntervalCalculatorHelper().nowTimestampSecond()),
            days_elapsed = IntervalCalculatorHelper().days_elapsed(creationTimestampSeconds),
            next_day_at = TimestampSecs(IntervalCalculatorHelper().next_day_at())
        )

        cardQueues!!.update_queues_after_answering_card(card, timing)
    }

    fun get_scheduling_states(flashcard: Flashcard): SchedulingStates {
        val updater = getCardStateUpdater(flashcard)
        return updater.get_scheduling_states()
    }

    fun getCardStateUpdater(flashcard: Flashcard): CardStateUpdater {
        val days_elapsed = get_days_elpased()

        return CardStateUpdaterBuilder().create(flashcard, days_elapsed)
    }

    fun get_days_elpased(): Long {
        return IntervalCalculatorHelper().days_elapsed(creationTimestampSeconds)
    }

    fun getNewCardState(flashcard: Flashcard, rating: Rating): CardState {

        val schedulingStates = get_scheduling_states(flashcard)

        return when(rating){
            Rating.Easy -> {
                schedulingStates.easy
            }
            Rating.Good -> {
                schedulingStates.good
            }
            Rating.Hard-> {
                schedulingStates.hard
            }
            Rating.Again -> {
                schedulingStates.again
            }
        }
    }

    suspend fun apply_new_state(answeredFlashcard: Flashcard, newState: CardState) = withContext(Dispatchers.IO) {

        val updater = getCardStateUpdater(answeredFlashcard)
        val currentState = updater.current_card_state()

        when(newState){

            is CardState.New -> {
                updater.apply_new_state(currentState, newState.newState)

                val answeredFlashcard = updater.flashcard
                flashcardUseCases.addFlashcard(answeredFlashcard)
                update_queues_after_answering_card(answeredFlashcard)
            }

            is CardState.Relearning -> {
                updater.apply_relearning_state(currentState, newState.relearnState)

                val answeredFlashcard = updater.flashcard
                flashcardUseCases.addFlashcard(answeredFlashcard)
                update_queues_after_answering_card(answeredFlashcard)
            }

            is CardState.Learning -> {
                updater.apply_learning_state(currentState, newState.learnState)

                val answeredFlashcard = updater.flashcard
                flashcardUseCases.addFlashcard(answeredFlashcard)
                update_queues_after_answering_card(answeredFlashcard)
            }

            is CardState.Review -> {
                updater.apply_review_state(currentState, newState.reviewState)

                val answeredFlashcard = updater.flashcard
                flashcardUseCases.addFlashcard(answeredFlashcard)
                update_queues_after_answering_card(answeredFlashcard)
            }

            null -> {
            }
        }
    }

    suspend fun answer_card(answeredFlashcard: Flashcard, rating: Rating) = withContext(Dispatchers.IO) {
        val newState = getNewCardState(answeredFlashcard, rating)
        apply_new_state(answeredFlashcard, newState)
    }
}
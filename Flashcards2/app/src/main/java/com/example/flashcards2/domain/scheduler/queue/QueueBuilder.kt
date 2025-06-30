package com.example.flashcards2.domain.scheduler.queue

import com.example.flashcards2.domain.model.Flashcard
import com.example.flashcards2.domain.scheduler.card_state_updater.IntervalCalculatorHelper
import com.example.flashcards2.domain.scheduler.timestamp.TimestampSecs
import com.example.flashcards2.domain.use_case.flashcard.FlashcardUseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QueueBuilder(val flashcardUseCases: FlashcardUseCases) {

    suspend fun build(learn_ahead_secs: Long, creationTimestampSeconds: Long, groupId: Long): CardQueues = withContext(Dispatchers.IO){

        val flashcardListsHolder = loadFlashcards(creationTimestampSeconds, groupId)

        val intraday_learning = sort_learning(flashcardListsHolder.learning)
        val now = TimestampSecs().now()
        val cutoff = now.adding_secs(learn_ahead_secs)
        val learn_count = intraday_learning
            .takeWhile { it.due <= cutoff.value  }
            .count() + flashcardListsHolder.day_learning.size

        val review_count = flashcardListsHolder.review.size
        val new_count = flashcardListsHolder.new.size

        val with_interday_learn = flashcardListsHolder.review + flashcardListsHolder.day_learning
        val main_iter = with_interday_learn + flashcardListsHolder.new

         CardQueues(
            intraday_learning = intraday_learning.toMutableList(),
            main = main_iter.toMutableList(),
            counts = Counts(
                new = new_count,
                learning = learn_count,
                review = review_count
            ),
            current_learning_cutoff = cutoff,
            learn_ahead_secs = 1200
        )
    }


    private suspend fun loadFlashcards(creationTimestampSeconds: Long, groupId: Long): FlashcardListsHolder = withContext(Dispatchers.IO) {
        val daysSinceColCreation = IntervalCalculatorHelper().days_elapsed(creationTimestampSeconds)
       return@withContext FlashcardsLoader(flashcardUseCases, daysSinceColCreation, groupId).gather_cards()
    }

    fun sort_learning(learning: List<Flashcard>): List<Flashcard> {
        return learning.sortedBy { it.due }
    }
}
package com.example.flashcards2.domain.scheduler.queue

import com.example.flashcards2.domain.model.Flashcard
import com.example.flashcards2.domain.scheduler.card_state_updater.IntervalCalculatorHelper
import com.example.flashcards2.domain.scheduler.timestamp.TimestampSecs
import com.example.flashcards2.domain.use_case.flashcard.FlashcardUseCases
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext


class FlashcardsLoader(
    val flashcardUseCases: FlashcardUseCases,
    val daysSinceColCreation: Long,
    val groupId: Long
) {

    suspend fun gather_cards(): FlashcardListsHolder = withContext(Dispatchers.IO) {

        val day_learning_deferred = async { gather_intraday_learning_cards().first() }
        val learning_deferred = async { gather_due_cards(DueCardKind.Learning).first() }
        val review_deferred = async { gather_due_cards(DueCardKind.Review).first() }
        val new_deferred = async { gather_new_cards().first() }


        FlashcardListsHolder(
            new = new_deferred.await(),
            review = review_deferred.await(),
            learning = learning_deferred.await(),
            day_learning = day_learning_deferred.await()
        )
    }

    private fun gather_new_cards(): Flow<List<Flashcard>> {
        return flashcardUseCases.getNewFlashcards(groupId)
    }

    private fun gather_due_cards(kind: DueCardKind): Flow<List<Flashcard>> {
        when(kind){
            DueCardKind.Review -> {
                return flashcardUseCases.getDueReviewFlashcards(daysSinceColCreation, groupId)
            }

            DueCardKind.Learning -> {
                return flashcardUseCases.getDueLearnFlashcards(TimestampSecs().now().value, groupId)
            }
        }
    }

    private fun gather_intraday_learning_cards(): Flow<List<Flashcard>> {
        val next_day_at = IntervalCalculatorHelper().next_day_at()
        val learn_cutoff = TimestampSecs(next_day_at)
        return flashcardUseCases.getIntradayLearningFlashcards(learn_cutoff.value, groupId)
    }
}
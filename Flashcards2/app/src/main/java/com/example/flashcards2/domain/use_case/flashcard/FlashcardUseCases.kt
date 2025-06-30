package com.example.flashcards2.domain.use_case.flashcard

data class FlashcardUseCases(
    val addFlashcard: AddFlashcard,
    val deleteFlashcard: DeleteFlashcard,
    val getFlashcards: GetFlashcards,
    val getFlashcardById: GetFlashcardById,
    val getFlashcardsByGroupId: GetFlashcardsByGroupId,
    val deleteFlashcardsByGroupId: DeleteFlashcardsByGroupId,
    val deleteFlashcardById: DeleteFlashcardById,
    val getFlashcardsWhereRepetitionTimeIsSmallerThan: GetFlashcardsWhereRepetitionTimeIsSmallerThan,
    val getIntradayLearningFlashcards: GetIntradayLearningFlashcards,
    val getDueLearnFlashcards: GetDueLearnFlashcards,
    val getDueReviewFlashcards: GetDueReviewFlashcards,
    val getNewFlashcards: GetNewFlashcards
)

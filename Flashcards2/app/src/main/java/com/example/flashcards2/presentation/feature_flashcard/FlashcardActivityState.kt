package com.example.flashcards2.presentation.feature_flashcard


data class FlashcardActivityState(
    val text: String = "",
    val flipButtonVisibility: Boolean = true,
    val rateButtonsVisibility: Boolean = false
)
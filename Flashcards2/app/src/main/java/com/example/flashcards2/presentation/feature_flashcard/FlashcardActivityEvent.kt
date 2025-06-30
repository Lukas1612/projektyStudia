package com.example.flashcards2.presentation.feature_flashcard

sealed class FlashcardActivityEvent {
    object ClickedFlipFlashcard: FlashcardActivityEvent()
    data class ClickedRateButton(val buttonType: ButtonType): FlashcardActivityEvent()
}
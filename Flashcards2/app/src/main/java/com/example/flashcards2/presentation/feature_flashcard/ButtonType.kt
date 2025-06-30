package com.example.flashcards2.presentation.feature_flashcard

sealed class ButtonType{
    object RepeatButton: ButtonType()
    object HardButton: ButtonType()
    object GoodButton: ButtonType()
    object EasyButton: ButtonType()
}
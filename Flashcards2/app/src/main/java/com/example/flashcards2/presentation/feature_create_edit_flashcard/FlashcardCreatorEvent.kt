package com.example.flashcards2.presentation.feature_create_edit_flashcard

sealed class FlashcardCreatorEvent {
    data class SelectedFlashcardGroup(val value: String): FlashcardCreatorEvent()
    data class EnteredWord(val value: String): FlashcardCreatorEvent()
    data class EnteredTranslation(val value: String): FlashcardCreatorEvent()
    data class SaveFlashcardGroup(val value: String): FlashcardCreatorEvent()
    object OpenFlashcardGroupCreator: FlashcardCreatorEvent()
    object SaveFlashcard: FlashcardCreatorEvent()
    object DeleteFlashcard: FlashcardCreatorEvent()
    object SelectedExpandAllGroupsList: FlashcardCreatorEvent()
}
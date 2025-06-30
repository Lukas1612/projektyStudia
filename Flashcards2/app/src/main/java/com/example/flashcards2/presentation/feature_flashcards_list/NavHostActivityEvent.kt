package com.example.flashcards2.presentation.feature_flashcards_list

sealed class NavHostActivityEvent {
    data class GoToFlashcardListFragment(val groupId: Long): NavHostActivityEvent()
}
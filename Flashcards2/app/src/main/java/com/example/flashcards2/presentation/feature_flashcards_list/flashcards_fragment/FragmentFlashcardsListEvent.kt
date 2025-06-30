package com.example.flashcards2.presentation.feature_flashcards_list.flashcards_fragment

sealed class FragmentFlashcardsListEvent {
    object FabButtonClicked: FragmentFlashcardsListEvent()
    data class ShortClickedFlashcard(val id: Long): FragmentFlashcardsListEvent()
    data class LongClickedFlashcard(val id: Long): FragmentFlashcardsListEvent()
    object ClickedDeleteMenuItem: FragmentFlashcardsListEvent()
    object ClickedClearMenuItem: FragmentFlashcardsListEvent()
}
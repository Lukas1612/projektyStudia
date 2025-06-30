package com.example.flashcards2.presentation.feature_flashcards_list.groups_fragment


sealed class FlashcardGroupListEvent {
    object ClickedMainFabButton: FlashcardGroupListEvent()
    object ClickedGroupFabButton: FlashcardGroupListEvent()
    object ClickedFlashcardFabButton: FlashcardGroupListEvent()
    data class ShortClickedFlashcardGroup(val id: Long): FlashcardGroupListEvent()
    data class LongClickedFlashcardGroup(val id: Long): FlashcardGroupListEvent()
    object ClickedDeleteMenuItem: FlashcardGroupListEvent()
    object ClickedClearMenuItem: FlashcardGroupListEvent()
}
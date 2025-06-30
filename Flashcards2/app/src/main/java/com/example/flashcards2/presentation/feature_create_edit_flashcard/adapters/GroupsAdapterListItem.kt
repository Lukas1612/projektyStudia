package com.example.flashcards2.presentation.feature_create_edit_flashcard.adapters

import com.example.flashcards2.domain.model.FlashcardGroup

sealed class GroupsAdapterListItem {
    object GroupCreatorItem : GroupsAdapterListItem()
    data class FlashcardGroupItem(val group: FlashcardGroup) : GroupsAdapterListItem()
}
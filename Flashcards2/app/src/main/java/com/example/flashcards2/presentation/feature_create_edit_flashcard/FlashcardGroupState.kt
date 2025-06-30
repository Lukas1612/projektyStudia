package com.example.flashcards2.presentation.feature_create_edit_flashcard

import com.example.flashcards2.domain.model.FlashcardGroup
import com.example.flashcards2.presentation.feature_create_edit_flashcard.adapters.GroupsAdapterListItem

data class FlashcardGroupState (
    val items: List<GroupsAdapterListItem> = emptyList(),
    val selectedGroup: FlashcardGroup? = null,
    val groupsListExpanded: Boolean = false
)
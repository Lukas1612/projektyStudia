package com.example.flashcards2.presentation.feature_flashcards_list.groups_fragment.adapters

import com.example.flashcards2.domain.model.FlashcardGroup

data class GroupAdapterItem(
    val group: FlashcardGroup,
    val isSelected: Boolean
)

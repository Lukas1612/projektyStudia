package com.example.flashcards2.presentation.feature_flashcards_list.groups_fragment

import com.example.flashcards2.presentation.feature_flashcards_list.groups_fragment.adapters.GroupAdapterItem

data class FlashcardGroupListState(
    val items: List<GroupAdapterItem> = emptyList(),
    val recyclerViewVisibility: Boolean = false,
    val emptyListNoticeVisibility: Boolean = true,
    val expandedButtonsVisibility: Boolean = false,
    val isToolBarVisible: Boolean = false
)

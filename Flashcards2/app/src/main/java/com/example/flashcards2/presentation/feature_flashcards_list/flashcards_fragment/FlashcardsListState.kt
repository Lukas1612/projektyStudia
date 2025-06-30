package com.example.flashcards2.presentation.feature_flashcards_list.flashcards_fragment

import com.example.flashcards2.domain.model.Flashcard
import com.example.flashcards2.presentation.feature_flashcards_list.flashcards_fragment.adapters.FlashcardListAdapterItem

data class FlashcardsListState(
    val items: List<FlashcardListAdapterItem> = emptyList(),
    val groupId: Long? = null,
    val recyclerViewVisibility: Boolean = false,
    val emptyListNoticeVisibility: Boolean = true,
    val isToolBarVisible: Boolean = false
)

package com.example.flashcards2.presentation.feature_flashcards_list.flashcards_fragment.adapters

import com.example.flashcards2.domain.model.Flashcard

data class FlashcardListAdapterItem(
    val flashcard: Flashcard,
    val isSelected: Boolean
)

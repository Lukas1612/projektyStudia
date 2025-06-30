package com.example.flashcards2.presentation.feature_flashcards_list.flashcards_fragment.adapters

interface OnFlashcardClickListener {
    fun onShortClick(id: Long)
    fun onLongClick(id: Long)
}
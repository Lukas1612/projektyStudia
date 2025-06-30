package com.example.flashcards2.presentation.feature_flashcards_list.groups_fragment.adapters

interface OnFlashcardGroupItemClickListener {
    fun onShortClick(groupId: Long)
    fun onLongClick(groupId: Long)
}
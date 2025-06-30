package com.example.flashcards2.domain.scheduler.queue

import com.example.flashcards2.domain.model.Flashcard

data class FlashcardListsHolder(
    val new: List<Flashcard>,
    val review: List<Flashcard>,
    val learning: List<Flashcard>,
    val day_learning: List<Flashcard>,
){
    fun isHolderNotEmpty(): Boolean{
        return new.isNotEmpty()
                || review.isNotEmpty()
                || learning.isNotEmpty()
                || day_learning.isNotEmpty()
    }

}
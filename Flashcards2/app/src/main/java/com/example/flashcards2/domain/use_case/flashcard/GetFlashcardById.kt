package com.example.flashcards2.domain.use_case.flashcard

import com.example.flashcards2.domain.model.Flashcard
import com.example.flashcards2.domain.repository.FlashcardRepository

class GetFlashcardById(private val repository: FlashcardRepository) {

    suspend operator fun invoke(id: Long): Flashcard?
    {
        return repository.getFlashcardById(id)
    }
}
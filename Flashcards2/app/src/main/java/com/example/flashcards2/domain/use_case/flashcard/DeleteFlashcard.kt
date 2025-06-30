package com.example.flashcards2.domain.use_case.flashcard

import com.example.flashcards2.domain.model.Flashcard
import com.example.flashcards2.domain.repository.FlashcardRepository

class DeleteFlashcard(private val repository: FlashcardRepository) {

    suspend operator fun invoke(flashcard: Flashcard)
    {
        repository.deleteFlashcard(flashcard)
    }
}
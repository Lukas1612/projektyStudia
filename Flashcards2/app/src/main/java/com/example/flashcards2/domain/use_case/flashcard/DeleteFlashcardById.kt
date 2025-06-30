package com.example.flashcards2.domain.use_case.flashcard

import com.example.flashcards2.domain.repository.FlashcardRepository

class DeleteFlashcardById(private val repository: FlashcardRepository) {
    suspend operator fun invoke(id: Long) {
        repository.deleteFlashcardById(id)
    }
}
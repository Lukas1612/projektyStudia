package com.example.flashcards2.domain.use_case.flashcard

import com.example.flashcards2.domain.model.Flashcard
import com.example.flashcards2.domain.repository.FlashcardRepository
import kotlinx.coroutines.flow.Flow

class GetFlashcards(private val repository: FlashcardRepository) {
     operator fun invoke(): Flow<List<Flashcard>>{
        return repository.getFlashcards()
     }
}
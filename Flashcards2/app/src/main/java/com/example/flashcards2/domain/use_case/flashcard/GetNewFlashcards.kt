package com.example.flashcards2.domain.use_case.flashcard

import com.example.flashcards2.domain.model.Flashcard
import com.example.flashcards2.domain.repository.FlashcardRepository
import kotlinx.coroutines.flow.Flow

class GetNewFlashcards(private val repository: FlashcardRepository) {
    operator fun invoke(groupId: Long): Flow<List<Flashcard>> {
        return repository.gatherNewCards(groupId)
    }
}
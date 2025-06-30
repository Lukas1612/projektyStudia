package com.example.flashcards2.domain.use_case.groups

import com.example.flashcards2.domain.model.FlashcardGroup
import com.example.flashcards2.domain.repository.FlashcardGroupRepository
import kotlinx.coroutines.flow.Flow

class GetFlashcardGroups(private val repository: FlashcardGroupRepository) {
     operator fun invoke(): Flow<List<FlashcardGroup>>
    {
        return repository.getFlashcardGroups()
    }
}
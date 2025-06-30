package com.example.flashcards2.domain.use_case.groups

import com.example.flashcards2.domain.model.FlashcardGroup
import com.example.flashcards2.domain.repository.FlashcardGroupRepository

class GetFlashcardGroupByName(val repository: FlashcardGroupRepository) {
    suspend operator fun invoke(name: String): FlashcardGroup? {
        return repository.getFlashcardGroupByName(name)
    }
}
package com.example.flashcards2.domain.use_case.groups

import com.example.flashcards2.domain.model.FlashcardGroup
import com.example.flashcards2.domain.repository.FlashcardGroupRepository

class GetFlashcardGroupById(private val repository: FlashcardGroupRepository) {
    suspend operator fun invoke(id: Long): FlashcardGroup? {
        return repository.getFlashcardGroupById(id)
    }
}
package com.example.flashcards2.domain.use_case.groups

import com.example.flashcards2.domain.model.FlashcardGroup
import com.example.flashcards2.domain.repository.FlashcardGroupRepository

class DeleteFlashcardGroup(private val repository: FlashcardGroupRepository) {
    suspend operator fun invoke(flashcardGroup: FlashcardGroup){
        repository.deleteFlashcardGroup(flashcardGroup)
    }
}
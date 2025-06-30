package com.example.flashcards2.domain.use_case.groups

import com.example.flashcards2.domain.repository.FlashcardGroupRepository

class DeleteFlashcardGroupById(private val repository: FlashcardGroupRepository) {
    suspend operator fun invoke(groupId: Long){
        repository.deleteFlashcardGroupById(groupId)
    }
}
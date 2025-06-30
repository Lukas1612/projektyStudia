package com.example.flashcards2.domain.use_case.flashcard

import com.example.flashcards2.domain.repository.FlashcardRepository

class DeleteFlashcardsByGroupId(private val repository: FlashcardRepository) {
    suspend operator fun invoke(groupId: Long){
        repository.deleteFlashcardsByGroupId(groupId)
    }
}
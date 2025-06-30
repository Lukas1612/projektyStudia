package com.example.flashcards2.domain.repository

import com.example.flashcards2.domain.model.Flashcard
import com.example.flashcards2.domain.model.FlashcardGroup
import kotlinx.coroutines.flow.Flow

interface FlashcardGroupRepository {

    fun getFlashcardGroups(): Flow<List<FlashcardGroup>>
    suspend fun getFlashcardGroupById(id: Long): FlashcardGroup?
    suspend fun getFlashcardGroupByName(name: String): FlashcardGroup?
    suspend fun insertFlashcardGroup(flashcardGroup: FlashcardGroup): Long
    suspend fun deleteFlashcardGroup(flashcardGroup: FlashcardGroup)
    suspend fun deleteFlashcardGroupById(groupId: Long)
}
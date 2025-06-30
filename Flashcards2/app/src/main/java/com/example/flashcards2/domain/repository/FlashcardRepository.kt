package com.example.flashcards2.domain.repository

import com.example.flashcards2.data.entity.FlashcardEntity
import com.example.flashcards2.domain.model.Flashcard
import kotlinx.coroutines.flow.Flow

interface FlashcardRepository {
    fun getFlashcards(): Flow<List<Flashcard>>
    fun getFlashcardsWhereRepetitionTimeIsSmallerThan(timeInMilliseconds: Long): Flow<List<Flashcard>>
    fun getFlashcardsByGroupId(groupId: Long): Flow<List<Flashcard>>
    fun gatherIntradayLearningCards(currentTimestampSecs: Long, groupId: Long): Flow<List<Flashcard>>
    fun gatherDueLearnCards(nowSec: Long, groupId: Long): Flow<List<Flashcard>>
    fun gatherDueReviewCards(today: Long, groupId: Long): Flow<List<Flashcard>>
    fun gatherNewCards(groupId: Long): Flow<List<Flashcard>>
    suspend fun getFlashcardById(id: Long): Flashcard?
    suspend fun insertFlashcard(flashcard: Flashcard)
    suspend fun deleteFlashcardsByGroupId(id: Long)
    suspend fun deleteFlashcardById(id: Long)
    suspend fun deleteFlashcard(flashcard: Flashcard)
}
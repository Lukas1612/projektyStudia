package com.example.flashcards2.domain.repository

import com.example.flashcards2.domain.model.Col
import com.example.flashcards2.domain.model.FlashcardGroup
import kotlinx.coroutines.flow.Flow

interface ColRepository {
    fun getCols(): Flow<List<Col>>
    suspend fun insertCol(col: Col): Long
    suspend fun deleteCol(col: Col)
}
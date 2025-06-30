package com.example.flashcards2.domain.use_case.col

import com.example.flashcards2.domain.model.Col
import com.example.flashcards2.domain.repository.ColRepository
import kotlinx.coroutines.flow.Flow

class GetCol(val repository: ColRepository) {

    operator fun invoke(): Flow<List<Col>> {
        return repository.getCols()
    }
}
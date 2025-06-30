package com.example.flashcards2.domain.use_case.col

import com.example.flashcards2.domain.model.Col
import com.example.flashcards2.domain.repository.ColRepository

class DeleteCol(val repository: ColRepository) {
    suspend operator fun invoke(col: Col){
        repository.deleteCol(col)
    }
}
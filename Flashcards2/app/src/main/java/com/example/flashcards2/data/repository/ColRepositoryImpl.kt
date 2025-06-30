package com.example.flashcards2.data.repository

import com.example.flashcards2.data.data_source.ColDao
import com.example.flashcards2.data.entity.ColEntity
import com.example.flashcards2.domain.model.Col
import com.example.flashcards2.domain.repository.ColRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ColRepositoryImpl(
    private val dao: ColDao
): ColRepository {
    override fun getCols(): Flow<List<Col>> {
        val colEntity =  dao.getCol()

        return colEntity.map { list ->
            list.map { entity ->
                entity.toCol()
            }
        }
    }

    override suspend fun insertCol(col: Col): Long {
        return dao.insertCol(toColEntity(col))
    }

    override suspend fun deleteCol(col: Col) {
        dao.deleteCol(toColEntity(col))
    }


    private fun toColEntity(col: Col): ColEntity {

        if(col.id == null)
        {
            return ColEntity(
                creation_timestamp_seconds = col.creationTimestampSeconds
            )
        }else
        {
            return ColEntity(
                id = col.id,
                creation_timestamp_seconds = col.creationTimestampSeconds
                )
        }
    }
}
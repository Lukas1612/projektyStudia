package com.example.flashcards2.data.repository

import com.example.flashcards2.data.data_source.FlashcardGroupDao
import com.example.flashcards2.data.entity.FlashcardGroupEntity
import com.example.flashcards2.domain.model.FlashcardGroup
import com.example.flashcards2.domain.repository.FlashcardGroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FlashcardGroupRepositoryImpl(
    private val dao: FlashcardGroupDao
): FlashcardGroupRepository{
    override fun getFlashcardGroups(): Flow<List<FlashcardGroup>> {
        val groupEntities = dao.getFlashcardGroups()

        return groupEntities.map { list ->
            list.map { entity ->
                entity.toFlashcardGroup()
            }
        }
    }

    override suspend fun getFlashcardGroupById(id: Long): FlashcardGroup? {
        return dao.getFlashcardGroupById(id)?.toFlashcardGroup()
    }

    override suspend fun getFlashcardGroupByName(name: String): FlashcardGroup? {
        return dao.getFlashcardGroupByName(name)?.toFlashcardGroup()
    }

    override suspend fun insertFlashcardGroup(flashcardGroup: FlashcardGroup): Long {
        val groupEntity = toFlashcardGroupEntity(flashcardGroup)

        return dao.insertFlashcardGroup(groupEntity)
    }

    override suspend fun deleteFlashcardGroup(flashcardGroup: FlashcardGroup) {
        val groupEntity = toFlashcardGroupEntity(flashcardGroup)

        dao.deleteFlashcardGroup(groupEntity)
    }

    override suspend fun deleteFlashcardGroupById(groupId: Long) {
        dao.deleteFlashcardGroupById(groupId)
    }

    private fun toFlashcardGroupEntity(flashcardGroup: FlashcardGroup): FlashcardGroupEntity {

        if(flashcardGroup.id == null)
        {
            return FlashcardGroupEntity(
                name = flashcardGroup.name,
                date = flashcardGroup.date
            )
        }else
        {
            return FlashcardGroupEntity(
                id = flashcardGroup.id,
                date = flashcardGroup.date,
                name = flashcardGroup.name
            )
        }
    }
}
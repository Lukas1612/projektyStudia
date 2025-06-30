package com.example.flashcards2.data.repository

import com.example.flashcards2.data.data_source.FlashcardDao
import com.example.flashcards2.data.entity.FlashcardEntity
import com.example.flashcards2.domain.model.Flashcard
import com.example.flashcards2.domain.repository.FlashcardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FlashcardRepositoryImpl(
    private val dao: FlashcardDao
): FlashcardRepository {
    override fun getFlashcards(): Flow<List<Flashcard>> {
        val flashcardEntities = dao.getFlashcards()

        return flashcardEntities.map { list ->
            list.map { entity ->
                entity.toFlashcard()
            }
        }
    }

    override fun getFlashcardsWhereRepetitionTimeIsSmallerThan(timeInMilliseconds: Long): Flow<List<Flashcard>> {
        val flashcardEntities = dao.getFlashcardsWhereTimeIsSmallerThan(timeInMilliseconds)

        return flashcardEntities.map { list ->
            list.map { entity ->
                entity.toFlashcard()
            }
        }
    }

    override fun getFlashcardsByGroupId(groupId: Long): Flow<List<Flashcard>> {
        val flashcardEntities = dao.getFlashcardsByGroupId(groupId)

        return flashcardEntities.map { list ->
            list.map { entity ->
                entity.toFlashcard()
            }
        }
    }

    override fun gatherIntradayLearningCards(currentTimestampSecs: Long, groupId: Long): Flow<List<Flashcard>> {
        val flashcardEntities = dao.gatherIntradayLearningCards(currentTimestampSecs, groupId)

        return flashcardEntities.map { list ->
            list.map { entity ->
                entity.toFlashcard()
            }
        }
    }

    override fun gatherDueLearnCards(nowSec: Long, groupId: Long): Flow<List<Flashcard>> {
        val flashcardEntities = dao.gatherDueLearnCards(nowSec, groupId)

        return flashcardEntities.map { list ->
            list.map { entity ->
                entity.toFlashcard()
            }
        }
    }

    override fun gatherDueReviewCards(today: Long, groupId: Long): Flow<List<Flashcard>> {
        val flashcardEntities = dao.gatherDueReviewCards(today, groupId)

        return flashcardEntities.map { list ->
            list.map { entity ->
                entity.toFlashcard()
            }
        }
    }

    override fun gatherNewCards(groupId: Long): Flow<List<Flashcard>> {
        val flashcardEntities = dao.gatherNewCards(groupId)

        return flashcardEntities.map { list ->
            list.map { entity ->
                entity.toFlashcard()
            }
        }
    }

    override suspend fun getFlashcardById(id: Long): Flashcard? {
        return dao.getFlashcardById(id)?.toFlashcard()
    }

    override suspend fun insertFlashcard(flashcard: Flashcard) {
        val flashcardEntity = toFlashcardEntity(flashcard)
        dao.insertFlashcard(flashcardEntity)
    }

    override suspend fun deleteFlashcardsByGroupId(id: Long) {
        dao.deleteFlashcardsByGroupId(id)
    }

    override suspend fun deleteFlashcardById(id: Long) {
        dao.deleteFlashcardById(id)
    }

    override suspend fun deleteFlashcard(flashcard: Flashcard) {
        val flashcardEntity = toFlashcardEntity(flashcard)
        dao.deleteFlashcard(flashcardEntity)
    }

    private fun toFlashcardEntity(flashcard: Flashcard): FlashcardEntity{

        if(flashcard.id == null)
        {
            return FlashcardEntity(
                front = flashcard.front,
                back = flashcard.back,
                group_id = flashcard.group_id,
                interval = flashcard.interval,
                ctype = flashcard.ctype.ordinal,
                queue = flashcard.queue.ordinal,
                due = flashcard.due,
                ease_factor = flashcard.ease_factor,
                reps = flashcard.reps,
                lapses = flashcard.lapses,
                remaining_steps = flashcard.remaining_steps,
                original_due = flashcard.original_due,
                original_deck_id = flashcard.original_deck_id,
                original_position = flashcard.original_position,
                desired_retention = flashcard.desired_retention,
                custom_data = flashcard.custom_data)
        }else
        {
            return FlashcardEntity(
                id = flashcard.id,
                front = flashcard.front,
                back = flashcard.back,
                group_id = flashcard.group_id,
                interval = flashcard.interval,
                ctype = flashcard.ctype.ordinal,
                queue = flashcard.queue.ordinal,
                due = flashcard.due,
                ease_factor = flashcard.ease_factor,
                reps = flashcard.reps,
                lapses = flashcard.lapses,
                remaining_steps = flashcard.remaining_steps,
                original_due = flashcard.original_due,
                original_deck_id = flashcard.original_deck_id,
                original_position = flashcard.original_position,
                desired_retention = flashcard.desired_retention,
                custom_data = flashcard.custom_data)
        }
    }
}
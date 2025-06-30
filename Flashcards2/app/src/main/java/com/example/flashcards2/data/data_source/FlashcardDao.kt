package com.example.flashcards2.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flashcards2.data.entity.FlashcardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {

    @Query("SELECT * FROM flashcardentity")
    fun getFlashcards(): Flow<List<FlashcardEntity>>

    //************change************
    @Query("SELECT * FROM flashcardentity WHERE due <= :timeInMilliSeconds")
    fun getFlashcardsWhereTimeIsSmallerThan(timeInMilliSeconds: Long): Flow<List<FlashcardEntity>>
    //******************************

    @Query("SELECT * FROM flashcardentity WHERE group_id = :groupId AND due > 0 AND due <= :currentTimestampSecs AND queue = 1")
    fun gatherIntradayLearningCards(currentTimestampSecs: Long, groupId: Long): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcardentity WHERE group_id = :groupId AND due <= :nowSec AND queue = 1")
    fun gatherDueLearnCards(nowSec: Long, groupId: Long): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcardentity WHERE group_id = :groupId AND due <= :today AND queue = 2")
    fun gatherDueReviewCards(today: Long, groupId: Long): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcardentity WHERE group_id = :groupId AND queue = 0")
    fun gatherNewCards(groupId: Long): Flow<List<FlashcardEntity>>



    @Query("SELECT * FROM flashcardentity WHERE flashcardentity.group_id = :id")
    fun getFlashcardsByGroupId(id: Long): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcardentity WHERE id = :id")
    suspend fun getFlashcardById(id: Long): FlashcardEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(flashcardEntity: FlashcardEntity)

    @Query("DELETE FROM flashcardentity WHERE group_id = :id")
    suspend fun deleteFlashcardsByGroupId(id: Long)

    @Query("DELETE FROM flashcardentity WHERE id = :id")
    suspend fun deleteFlashcardById(id: Long)

    @Delete
    suspend fun deleteFlashcard(flashcardEntity: FlashcardEntity)
}
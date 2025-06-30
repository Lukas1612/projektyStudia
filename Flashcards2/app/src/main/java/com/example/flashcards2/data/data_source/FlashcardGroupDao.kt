package com.example.flashcards2.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flashcards2.data.entity.FlashcardGroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardGroupDao {

    @Query("SELECT * FROM flashcardgroupentity ORDER BY date DESC")
    fun getFlashcardGroups(): Flow<List<FlashcardGroupEntity>>

    @Query("SELECT * FROM flashcardgroupentity WHERE id = :id")
    suspend fun getFlashcardGroupById(id: Long): FlashcardGroupEntity?

    @Query("SELECT * FROM flashcardgroupentity WHERE name = :name")
    suspend fun getFlashcardGroupByName(name: String): FlashcardGroupEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcardGroup(flashcardGroupEntity: FlashcardGroupEntity): Long

    @Delete
    suspend fun deleteFlashcardGroup(flashcardGroupEntity: FlashcardGroupEntity)

    @Query("DELETE FROM flashcardgroupentity WHERE id = :id")
    suspend fun deleteFlashcardGroupById(id: Long)
}
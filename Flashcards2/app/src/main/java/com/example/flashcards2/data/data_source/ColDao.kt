package com.example.flashcards2.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flashcards2.data.entity.ColEntity
import com.example.flashcards2.data.entity.FlashcardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ColDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCol(colEntity: ColEntity): Long

    @Query("SELECT * FROM colentity")
     fun getCol(): Flow<List<ColEntity>>

    @Delete
    suspend fun deleteCol(colEntity: ColEntity)

}
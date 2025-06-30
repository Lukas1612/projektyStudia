package com.example.flashcards2.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.flashcards2.data.entity.ColEntity
import com.example.flashcards2.data.entity.FlashcardEntity
import com.example.flashcards2.data.entity.FlashcardGroupEntity

@Database(
    entities = [
        FlashcardEntity::class,
        FlashcardGroupEntity::class,
        ColEntity::class
    ],
    version = 1
)
abstract class FlashcardDatabase: RoomDatabase() {

    abstract val colDao: ColDao
    abstract val flashcardDao: FlashcardDao
    abstract val flashcardGroupDao: FlashcardGroupDao

    companion object {
        const val DATABASE_NAME = "flashcard_database"
    }
}
package com.example.flashcards2.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flashcards2.domain.model.Col
import com.example.flashcards2.domain.model.Flashcard

@Entity
data class ColEntity (
    @PrimaryKey val id: Long? = null,
    var creation_timestamp_seconds: Long
){
    fun toCol(): Col {
        return Col(
            id = id,
            creationTimestampSeconds = creation_timestamp_seconds
        )
    }
}
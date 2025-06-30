package com.example.flashcards2.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.flashcards2.domain.model.FlashcardGroup

@Entity(indices = [Index(value = ["name"], unique = true)])
data class FlashcardGroupEntity(
    @PrimaryKey val id: Long? = null,
    var name: String,
    var date: Long
){
    fun toFlashcardGroup(): FlashcardGroup{
        return FlashcardGroup(
            id = id,
            name = name,
            date = date
        )
    }
}

package com.example.flashcards2.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flashcards2.domain.model.Flashcard
import com.example.flashcards2.domain.scheduler.card_state_updater.CardType
import com.example.flashcards2.domain.scheduler.queue.CardQueue

@Entity
data class FlashcardEntity(
    @PrimaryKey val id: Long? = null,
    var group_id: Long? = null,
    var front: String,
    var back: String,
    var ctype: Int,
    var queue: Int,
    var due: Int,
    var interval: Int,
    var ease_factor: Int,
    var reps: Int,
    var lapses: Int,
    var remaining_steps: Int,
    var original_due: Int,
    var original_deck_id: Int,
    /// The position in the new queue before leaving it.
    var original_position: Int?,
    var desired_retention: Float?,
    /// JSON object or empty; exposed through the reviewer for persisting custom
    /// state
    var custom_data: String
){
    fun toFlashcard(): Flashcard{
        return Flashcard(
            id = id,
            front = front,
            back = back,
            group_id = group_id,
            ctype = fromIntToCtype(ctype),
            queue = fromIntToCardQueue(queue),
            due = due,
            interval = interval,
            ease_factor = ease_factor,
            reps = reps,
            lapses = lapses,
            remaining_steps = remaining_steps,
            original_due = original_due,
            original_deck_id = original_deck_id,
            original_position = original_position,
            desired_retention = desired_retention,
            custom_data =custom_data
        )
    }

    private fun fromIntToCtype(value: Int): CardType {
        return when (value){
            0 -> {
                CardType.New
            }
            1 -> {
                CardType.Learn
            }
            2 -> {
                CardType.Review
            }
            3 -> {
                CardType.Relearn
            }

            else -> {
                CardType.New
            }
        }
    }

    private fun fromIntToCardQueue(value: Int): CardQueue {
        return when (value){
            0 -> {
                CardQueue.New
            }
            1 -> {
                CardQueue.Learn
            }
            2 -> {
                CardQueue.Review
            }
            3 -> {
                CardQueue.DayLearn
            }

            else -> {
                CardQueue.New
            }
        }
    }
}

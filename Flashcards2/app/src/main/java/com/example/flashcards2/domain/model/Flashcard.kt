package com.example.flashcards2.domain.model

import com.example.flashcards2.domain.scheduler.card_state_updater.CardType
import com.example.flashcards2.domain.scheduler.queue.CardQueue
import javax.inject.Inject


/*
repetitions - this is the number of times a user sees a flashcard.
              0 means they haven't studied it yet, 1 means it is their first time, and so on.
              It is also referred to as n in some of the documentation.

quality - also known as quality of assessment. This is how difficult (as defined by the user) a flashcard is. The scale is from 0 to 5.
  After each repetition assess the quality of repetition response in 0-5 grade scale:
  5 – perfect response
  4 – correct response after a hesitation
  3 – correct response recalled with serious difficulty
  2 – incorrect response; where the correct one seemed easy to recall
  1 – incorrect response; the correct one remembered
  0 – complete blackout.

easiness - this is also referred to as the easiness factor or EFactor or EF.
           It is multiplier used to increase the "space" in spaced repetition. The range is from 1.3 to 2.5.

interval - this is the length of time (in days) between repetitions. It is the "space" of spaced repetition.

nextPractice - This is the date/time of when the flashcard comes due to review again.
 */


/*
priority:
0 means low
1 means normal
2 means higher

probability of drawing flashcard equals: low/normal/high = 1/2/4
 */
class Flashcard @Inject constructor(
    val id: Long? = null,
    var group_id: Long? = null,
    var front: String,
    var back: String,
    var ctype: CardType,
    var queue: CardQueue,
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
) {

    fun ease_factor(): Float {
        return ease_factor / 1000.0F
    }

    fun remaining_steps(): Int {
        return remaining_steps % 1000
    }

    fun is_intraday_learning(): Boolean {
        return queue == CardQueue.Learn
    }

    override fun toString(): String {
        val queueNum = queue.ordinal
        return "Flashcard(id=$id, group_id=$group_id, front='$front', back='$back', ctype=$ctype, queue=$queue, due=$due, interval=$interval, ease_factor=$ease_factor, reps=$reps, lapses=$lapses, remaining_steps=$remaining_steps, original_due=$original_due, original_deck_id=$original_deck_id, original_position=$original_position, desired_retention=$desired_retention, custom_data='$custom_data')"
    }

}

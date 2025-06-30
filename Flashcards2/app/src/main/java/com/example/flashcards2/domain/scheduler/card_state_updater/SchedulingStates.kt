package com.example.flashcards2.domain.scheduler.card_state_updater

import com.example.flashcards2.domain.scheduler.states.CardState


class SchedulingStates(
    val current: CardState,
    val again: CardState,
    val hard: CardState,
    val good: CardState,
    val easy: CardState
) {
    fun asString(): List<String>{

        return AnswersIntervalsStringBuilder()
            .describe_next_states(this)
    }
}


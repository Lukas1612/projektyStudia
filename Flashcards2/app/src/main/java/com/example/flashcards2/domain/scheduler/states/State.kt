package com.example.flashcards2.domain.scheduler.states

import com.example.flashcards2.domain.scheduler.card_state_updater.IntervalKind
import com.example.flashcards2.domain.scheduler.card_state_updater.SchedulingStates
import com.example.flashcards2.domain.scheduler.card_state_updater.StateContext


interface State {
    fun next_states(ctx: StateContext): SchedulingStates
    fun interval_kind(): IntervalKind

}
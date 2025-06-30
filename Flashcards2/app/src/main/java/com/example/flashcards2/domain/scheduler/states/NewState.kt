package com.example.flashcards2.domain.scheduler.states

import com.example.flashcards2.domain.scheduler.card_state_updater.CardStateConverter
import com.example.flashcards2.domain.scheduler.card_state_updater.IntervalKind
import com.example.flashcards2.domain.scheduler.card_state_updater.SchedulingStates
import com.example.flashcards2.domain.scheduler.card_state_updater.StateContext


class NewState(val position: Int): State {
    override fun next_states(ctx: StateContext): SchedulingStates {
        // New state acts like answering a failed learning card
        val next_states = LearnState(
            remaining_steps = ctx.steps.remaining_for_failed(),
            scheduled_secs = 0,
            elapsed_secs = 0
        ).next_states(ctx)

        // .. but with current as New, not Learning
        return SchedulingStates(
            current = CardStateConverter().fromNewStateToCardState(this),
            again = next_states.again,
            hard = next_states.hard,
            good =  next_states.good,
            easy = next_states.easy
        )
    }

    override fun interval_kind(): IntervalKind {
        return IntervalKind.InSecs(0)
    }

}
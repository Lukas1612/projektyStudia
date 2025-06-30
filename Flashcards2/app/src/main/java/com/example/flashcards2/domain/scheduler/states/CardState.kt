package com.example.flashcards2.domain.scheduler.states

import com.example.flashcards2.domain.scheduler.card_state_updater.IntervalKind
import com.example.flashcards2.domain.scheduler.card_state_updater.SchedulingStates
import com.example.flashcards2.domain.scheduler.card_state_updater.StateContext


sealed class CardState {
    data class Relearning(val relearnState: RelearnState): CardState()
    data class Review(val reviewState: ReviewState): CardState()
    data class New(val newState: NewState): CardState()
    data class Learning(val learnState: LearnState): CardState()

    fun next_states(ctx: StateContext): SchedulingStates {
        return when(this){
            is New -> {
                this.newState.next_states(ctx)
            }
            is Relearning -> {
                this.relearnState.next_states(ctx)
            }
            is Learning -> {
                this.learnState.next_states(ctx)
            }
            is Review -> {
                this.reviewState.next_states(ctx)
            }
        }
    }

    fun interval_kind(): IntervalKind {
        return when(this){
            is New -> {
                this.newState.interval_kind()
            }
            is Relearning -> {
                this.relearnState.interval_kind()
            }
            is Learning -> {
                this.learnState.interval_kind()
            }
            is Review -> {
                this.reviewState.interval_kind()
            }
        }
    }

    fun new_position(): Int?{

        return when(this){
            is New -> {
                this.newState.position
            }

            else ->{
                return null
            }
        }

    }
}
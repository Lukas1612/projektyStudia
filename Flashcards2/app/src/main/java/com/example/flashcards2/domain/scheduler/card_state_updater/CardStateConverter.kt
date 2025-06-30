package com.example.flashcards2.domain.scheduler.card_state_updater

import com.example.flashcards2.domain.scheduler.states.CardState
import com.example.flashcards2.domain.scheduler.states.LearnState
import com.example.flashcards2.domain.scheduler.states.NewState
import com.example.flashcards2.domain.scheduler.states.RelearnState
import com.example.flashcards2.domain.scheduler.states.ReviewState


class CardStateConverter {

    fun fromReviewStateToCardState(reviewState: ReviewState): CardState {
        return CardState.Review(reviewState)
    }

    fun fromLearnStateToCardState(learnState: LearnState): CardState {
        return CardState.Learning(learnState)
    }

    fun fromNewStateToCardState(newState: NewState): CardState {
        return CardState.New(newState)
    }

    fun fromRelearnStateToCardState(relearnState: RelearnState): CardState {
        return CardState.Relearning(relearnState)
    }
}
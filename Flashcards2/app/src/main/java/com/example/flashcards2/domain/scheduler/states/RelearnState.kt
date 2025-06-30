package com.example.flashcards2.domain.scheduler.states

import android.util.Log
import com.example.flashcards2.domain.scheduler.card_state_updater.CardStateConverter
import com.example.flashcards2.domain.scheduler.card_state_updater.IntervalKind
import com.example.flashcards2.domain.scheduler.card_state_updater.SchedulingStates
import com.example.flashcards2.domain.scheduler.card_state_updater.StateContext
import kotlin.math.max
import kotlin.math.round

class RelearnState(
    var learning: LearnState,
    var review: ReviewState,
): State {
    override fun next_states(ctx: StateContext): SchedulingStates {
        return SchedulingStates(
            current = CardStateConverter().fromRelearnStateToCardState(this),
            again = answer_again(ctx),
            hard = answer_hard(ctx),
            good = answer_good(ctx),
            easy = CardStateConverter().fromReviewStateToCardState(answer_easy(ctx))
        )
    }



    private fun answer_again(ctx: StateContext): CardState {

        val scheduled_days = review.failing_review_interval(ctx)
        val again_delay = ctx.relearn_steps.again_delay_secs_learn()


        
        if(again_delay != null){
            val relearnState = RelearnState(
                learning = LearnState(
                    remaining_steps = ctx.relearn_steps.remaining_for_failed(),
                    scheduled_secs = again_delay,
                    elapsed_secs = 0
                ),
                review = ReviewState(
                    scheduled_days = max(round(scheduled_days), 1.0f).toInt(),
                    elapsed_days = 0
                )
            )

            Log.d("tttttt", "RelearnState again_delay: $again_delay")
            Log.d("tttttt", "RelearnState answer_again  again_delay != null -> RelearnState")
            return CardStateConverter().fromRelearnStateToCardState(relearnState)
        }else{
            Log.d("tttttt", "RelearnState answer_again  again_delay == null -> ReviewState")
            return CardStateConverter().fromReviewStateToCardState(review)
        }

    }

    private fun answer_hard(ctx: StateContext): CardState {

        val hard_delay = ctx.relearn_steps.hard_delay_secs(learning.remaining_steps)

        if(hard_delay != null){
            val relearnState = RelearnState(
                learning = LearnState(
                    remaining_steps = learning.remaining_steps,
                    scheduled_secs = hard_delay,
                    elapsed_secs = learning.elapsed_secs
                ),
                review = ReviewState(
                    scheduled_days = review.scheduled_days,
                    elapsed_days = 0,
                    ease_factor = review.ease_factor,
                    lapses = review.lapses,
                    leeched = review.leeched
                )
            )

            Log.d("tttttt", "RelearnState answer_hard  hard_delay != null -> RelearnState")
            return CardStateConverter().fromRelearnStateToCardState(relearnState)
        }else{
            Log.d("tttttt", "RelearnState answer_hard  hard_delay == null -> ReviewState")
            return CardStateConverter().fromReviewStateToCardState(review)
        }
    }

    private fun answer_good(ctx: StateContext): CardState {
        val good_delay =  ctx
            .relearn_steps
            .good_delay_secs(learning.remaining_steps)


        if(good_delay != null){

            val relearnState = RelearnState(
                learning = LearnState(
                    scheduled_secs = good_delay,
                    remaining_steps = ctx
                        .relearn_steps
                        .remaining_for_good(learning.remaining_steps),
                    elapsed_secs = 0

                ),
                review = ReviewState(
                    scheduled_days = review.scheduled_days,
                    elapsed_days = 0,
                    ease_factor = review.ease_factor,
                    lapses = review.lapses,
                    leeched = review.leeched
                )
            )
            Log.d("tttttt", "RelearnState answer_good  good_delay != null -> RelearnState")
            return CardStateConverter().fromRelearnStateToCardState(relearnState)

        }else{
            Log.d("tttttt", "RelearnState answer_good  good_delay == null -> ReviewState")
            return CardStateConverter().fromReviewStateToCardState(review)
        }

    }

    private fun answer_easy(ctx: StateContext): ReviewState {

        val scheduled_days = review.scheduled_days + 1

        val reviewState = ReviewState(
            scheduled_days = scheduled_days,
            elapsed_days = 0,
            ease_factor = review.ease_factor,
            lapses = review.lapses,
            leeched = review.leeched
        )
        Log.d("tttttt", "RelearnState answer_easy  -> ReviewState")
        return reviewState
    }

    override fun interval_kind(): IntervalKind {
        return learning.interval_kind()
    }

}
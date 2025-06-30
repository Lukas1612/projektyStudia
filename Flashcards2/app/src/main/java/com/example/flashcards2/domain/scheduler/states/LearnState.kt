package com.example.flashcards2.domain.scheduler.states

import android.util.Log
import com.example.flashcards2.domain.scheduler.card_state_updater.CardStateConverter
import com.example.flashcards2.domain.scheduler.card_state_updater.IntervalKind
import com.example.flashcards2.domain.scheduler.card_state_updater.SchedulingStates
import com.example.flashcards2.domain.scheduler.card_state_updater.StateContext
import kotlin.math.max
import kotlin.math.round

class LearnState(
    var remaining_steps: Int,
    var scheduled_secs: Int,
    var elapsed_secs: Int,
): State {

    override fun next_states(ctx: StateContext): SchedulingStates {

        return SchedulingStates(
            current = CardStateConverter().fromLearnStateToCardState(this),
            again = answer_again(ctx),
            hard = answer_hard(ctx),
            good = answer_good(ctx),
            easy = CardStateConverter().fromReviewStateToCardState(answer_easy(ctx))
        )
    }

    private fun answer_easy(ctx: StateContext): ReviewState {
        val (minimum, maximum) = ctx.min_and_max_review_intervals(1)
        val interval = ctx.graduating_interval_easy

        Log.d("tttttt", "LearnState answer_easy -> ReviewState")

        return ReviewState(
            scheduled_days = ctx.with_review_fuzz(interval.toFloat(), minimum, maximum),
            ease_factor = ctx.initial_ease_factor
        )
    }

    private fun answer_good(ctx: StateContext): CardState {

        val good_delay = ctx.steps.good_delay_secs(remaining_steps)

        if(good_delay != null){
            val learnState = LearnState(
                remaining_steps = ctx.steps.remaining_for_good(remaining_steps),
                scheduled_secs = good_delay,
                elapsed_secs = 0
            )

            Log.d("tttttt", "LearnState answer_good  good_delay != null -> LearnState")

            return CardStateConverter().fromLearnStateToCardState(learnState)
        }else{
            val (minimum, maximum) = ctx.min_and_max_review_intervals(1)
            val interval = ctx.graduating_interval_good.toFloat()

            val reviewState = ReviewState(
                scheduled_days = ctx.with_review_fuzz(
                    max(round(interval), 1.0f),
                    minimum,
                    maximum
                ),
                ease_factor = ctx.initial_ease_factor
            )

            Log.d("tttttt", "LearnState answer_good  good_delay == null -> ReviewState")

            return CardStateConverter().fromReviewStateToCardState(reviewState)

        }
    }

    private fun answer_hard(ctx: StateContext): CardState {

       val hard_delay = ctx.steps.hard_delay_secs(remaining_steps)

        if(hard_delay != null){

            val learnState = LearnState(
                scheduled_secs = hard_delay,
                elapsed_secs = 0,
                remaining_steps = remaining_steps
            )

            Log.d("tttttt", "LearnState answer_hard  hard_delay != null -> LearnState")
            return CardStateConverter().fromLearnStateToCardState(learnState)
        }else{
            val (minimum, maximum) = ctx.min_and_max_review_intervals(1)

            val interval = ctx.graduating_interval_good.toFloat()

            val reviewState = ReviewState(
                scheduled_days = ctx.with_review_fuzz(
                    max(round(interval), 1.0f),
                    minimum,
                    maximum
                ),
                ease_factor = ctx.initial_ease_factor
            )

            Log.d("tttttt", "LearnState answer_hard  hard_delay == null -> ReviewState")

            return CardStateConverter().fromReviewStateToCardState(reviewState)
        }
    }

   /* private fun answer_again(ctx: StateContext): CardState {


        val again_delay = ctx.steps.again_delay_secs_learn()

        if(again_delay != null){

            val learnState = LearnState(
                remaining_steps = ctx.steps.remaining_for_failed(),
                scheduled_secs = again_delay,
                elapsed_secs = 0
            )

            Log.d("tttttt", "LearnState answer_again  again_delay != null -> LearnState")

            return CardStateConverter().fromLearnStateToCardState(learnState)
        }else{

            val (minimum, maximum) = ctx.min_and_max_review_intervals(1)

            val interval = ctx.graduating_interval_good.toFloat()

            val  reviewState = ReviewState(
                scheduled_days = ctx.with_review_fuzz(
                    max(round(interval), 1.0f),
                    minimum,
                    maximum
                ),
                ease_factor = ctx.initial_ease_factor
            )

            Log.d("tttttt", "LearnState answer_again  again_delay == null -> ReviewState")
            return CardStateConverter().fromReviewStateToCardState(reviewState)
        }
    }*/

    private fun answer_again(ctx: StateContext): CardState {


        val again_delay = ctx.steps.again_delay_secs_learn()

        val learnState = LearnState(
            remaining_steps = ctx.steps.remaining_for_failed(),
            scheduled_secs = again_delay!!,
            elapsed_secs = 0
        )

        return CardStateConverter().fromLearnStateToCardState(learnState)
    }


    override fun interval_kind(): IntervalKind {
        return IntervalKind.InSecs(scheduled_secs)
    }
}
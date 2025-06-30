package com.example.flashcards2.domain.scheduler.states

import com.example.flashcards2.domain.scheduler.card_state_updater.CardStateConverter
import com.example.flashcards2.domain.scheduler.card_state_updater.Constants.EASE_FACTOR_AGAIN_DELTA
import com.example.flashcards2.domain.scheduler.card_state_updater.Constants.EASE_FACTOR_EASY_DELTA
import com.example.flashcards2.domain.scheduler.card_state_updater.Constants.EASE_FACTOR_HARD_DELTA
import com.example.flashcards2.domain.scheduler.card_state_updater.Constants.INITIAL_EASE_FACTOR
import com.example.flashcards2.domain.scheduler.card_state_updater.Constants.MINIMUM_EASE_FACTOR
import com.example.flashcards2.domain.scheduler.card_state_updater.IntervalKind
import com.example.flashcards2.domain.scheduler.card_state_updater.SchedulingStates
import com.example.flashcards2.domain.scheduler.card_state_updater.StateContext

import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.round

//https://github.com/glutanimate/anki/blob/c9c7a3133c859c1ecd97940c0f1b9ccceb8c2a19/rslib/src/scheduler/states/review.rs#L4
//     StateContext::defaults_for_testing()

class ReviewState(
     var scheduled_days: Int = 0,
     var elapsed_days: Int = 0,
     var ease_factor: Float = INITIAL_EASE_FACTOR,
     var lapses: Int = 0,
     var leeched: Boolean = false
): State {


     data class Intervals(
        var hard: Int,
        var good: Int,
        var easy: Int
    )


    override fun next_states(ctx: StateContext): SchedulingStates {
        val intervals = passing_review_intervals(ctx)

        return SchedulingStates(
            current = CardStateConverter().fromReviewStateToCardState(this),
            again = answer_again(ctx),
            hard = CardStateConverter().fromReviewStateToCardState(answer_hard(intervals.hard)),
            good = CardStateConverter().fromReviewStateToCardState(answer_good(intervals.good)),
            easy = CardStateConverter().fromReviewStateToCardState(answer_easy(intervals.easy))
        )
    }



    fun passing_review_intervals(ctx: StateContext): Intervals { //**************
        return if (days_late() < 0) {
            passing_early_review_intervals(ctx)
        } else {
            passing_nonearly_review_intervals(ctx)
        }
    }

    private fun passing_nonearly_review_intervals(ctx: StateContext): Intervals {
        val current_interval: Float = scheduled_days.toFloat()
        val days_late: Float = max(days_late(), 0).toFloat()

        // hard
        val hard_factor = ctx.hard_multiplier
        val hard_minimum = if (hard_factor <= 1.0) {
             0
        } else {
            scheduled_days + 1
        }
        val hard_interval =
        constrain_passing_interval(ctx, current_interval * hard_factor, hard_minimum, true)

        // good
        val good_minimum = if (hard_factor <= 1.0) {
            scheduled_days + 1
        } else {
            hard_interval + 1
        }
        val good_interval = constrain_passing_interval(
            ctx,
            (current_interval + days_late / 2.0F) * ease_factor,
            good_minimum,
            true
        )

        // easy
        val easy_interval = constrain_passing_interval(
                ctx,
        (current_interval + days_late) * ease_factor * ctx.easy_multiplier,
        good_interval + 1,
            true
        )

        return Intervals(hard_interval, good_interval, easy_interval)
    }

    private fun passing_early_review_intervals(ctx: StateContext): Intervals {
        val scheduled = scheduled_days.toFloat()
        val elapsed = scheduled_days.toFloat() + days_late().toFloat()



        val factor = ctx.hard_multiplier
        val half_usual = factor / 2.0F
        val hard_interval =  constrain_passing_interval(
            ctx,
            max((elapsed * factor), (scheduled * half_usual)),
            0,
            false
            )

        val good_interval =
            constrain_passing_interval(ctx, max((elapsed * ease_factor), scheduled), 0, false)


        val reduced_bonus = ctx.easy_multiplier - (ctx.easy_multiplier - 1.0F) / 2.0F
        val easy_interval =  constrain_passing_interval(
            ctx,
            max((elapsed * ease_factor), scheduled) * reduced_bonus,
            0,
            false
            )


        return Intervals(hard_interval, good_interval, easy_interval)
    }

    /// Transform the provided hard/good/easy interval.
    // / - Apply fuzz.
    // / - Ensure it is at least `minimum`, and at least 1.
    // / - Ensure it is at or below the configured maximum interval.
     fun constrain_passing_interval(ctx: StateContext, _interval: Float, _minimum: Int, fuzz: Boolean): Int{
        val interval = _interval * ctx.interval_multiplier

        val (minimum, maximum) = ctx.min_and_max_review_intervals(_minimum)

        return if(fuzz){
            ctx.with_review_fuzz(interval, minimum, maximum)
        }else{
            interval.toInt().coerceIn(minimum, maximum)
        }
    }

     fun failing_review_interval(ctx: StateContext): Float{

         val (minimum, maximum) = ctx.min_and_max_review_intervals(ctx.minimum_lapse_interval)

        var interval = ctx.with_review_fuzz(
            scheduled_days.toFloat() * ctx.lapse_multiplier,
            minimum,
            maximum
        )

        return interval.toFloat()
    }

    private fun answer_again(ctx: StateContext): CardState {

        var _lapses = lapses + 1
        val leeched = leech_threshold_met(_lapses, ctx.leech_threshold)
        val scheduled_days = failing_review_interval(ctx)

        val again_review = ReviewState(
            scheduled_days  = max(round(scheduled_days), 1.0f).toInt(),
            elapsed_days = 0,
            ease_factor = max((ease_factor + EASE_FACTOR_AGAIN_DELTA), MINIMUM_EASE_FACTOR),
            lapses  = _lapses,
            leeched = leeched
        )

        val again_delay = ctx.relearn_steps.again_delay_secs_learn()


        if(again_delay != null){

            val relearnState = RelearnState(
                learning = LearnState(
                    remaining_steps = ctx.relearn_steps.remaining_for_failed(),
                    scheduled_secs = again_delay,
                    elapsed_secs = 0
                ),
                review = again_review
            )
            return CardState.Relearning(relearnState)

        }else{
            return CardState.Review(again_review)
        }
    }

    private fun answer_hard(scheduled_days: Int): ReviewState {

        return ReviewState(
            scheduled_days = scheduled_days,
            elapsed_days = 0,
            ease_factor = max((ease_factor + EASE_FACTOR_HARD_DELTA), MINIMUM_EASE_FACTOR),
            lapses = lapses,
            leeched = leeched
        )
    }

    private fun answer_good(scheduled_days: Int): ReviewState {
        return ReviewState (
            scheduled_days = scheduled_days,
            elapsed_days = 0,
            ease_factor = ease_factor,
            lapses = lapses,
            leeched = leeched
        )
    }

    private fun answer_easy(scheduled_days: Int): ReviewState {

        return ReviewState(
            scheduled_days = scheduled_days,
            elapsed_days = 0,
            ease_factor = ease_factor + EASE_FACTOR_EASY_DELTA,
            lapses = lapses,
            leeched = leeched
        )
    }

    fun leech_threshold_met(lapses: Int, threshold: Int): Boolean {
        if(threshold > 0){
            val half_threshold = max(ceil((threshold.toFloat() / 2.0F)), 1.0F).toInt()

            // at threshold, and every half threshold after that, rounding up
            return (lapses >= threshold && (lapses - threshold) % half_threshold == 0)
        }else{
            return false
        }
    }


    private fun days_late(): Int {
        return elapsed_days  - scheduled_days
    }

    override fun interval_kind(): IntervalKind {
        return IntervalKind.InDays(scheduled_days)
    }


}
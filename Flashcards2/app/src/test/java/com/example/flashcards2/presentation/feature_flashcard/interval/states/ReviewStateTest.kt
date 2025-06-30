package com.example.flashcards2.presentation.feature_flashcard.interval.states

import android.util.Log
import com.example.flashcards2.presentation.feature_flashcard.interval.LearningSteps
import com.example.flashcards2.presentation.feature_flashcard.interval.PreviewDelays
import com.example.flashcards2.presentation.feature_flashcard.interval.StateContext
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class ReviewStateTest{

    private fun defaultStateContext(): StateContext{
        return  StateContext(
            fuzz_factor = null,
            steps = LearningSteps(listOf(1.0f, 10.0f)),
            graduating_interval_good  =  1,
            graduating_interval_easy  =  4,
            initial_ease_factor  = 2.5F,
            hard_multiplier  =  1.2F,
            easy_multiplier  = 1.3F,
            interval_multiplier  = 1.0F,
            maximum_review_interval  =  36500,
            leech_threshold  =  8,
            relearn_steps = LearningSteps(listOf(10.0F)),
            lapse_multiplier  = 0.0F,
            minimum_lapse_interval  =  1,
            in_filtered_deck  = false,
            load_balancer_ctx = null,
            preview_delays = PreviewDelays(
                again = 1,
                hard = 10,
                good = 0
            )
        )
    }
    @Test
    fun passing_review_intervals(){

        val ctx = defaultStateContext()

        val reviewState = ReviewState(
            scheduled_days = 1,
            elapsed_days = 1,
            ease_factor = 1.3f,
            lapses = 0,
            leeched = false
        )
        ctx.fuzz_factor = 0.0f


        var output = listOf(reviewState.passing_review_intervals(ctx).hard, reviewState.passing_review_intervals(ctx).good, reviewState.passing_review_intervals(ctx).easy)
        var expected = listOf(2, 3, 4)
        assertEquals(output, expected)

        ctx.interval_multiplier = 0.1f
        output = listOf(reviewState.passing_review_intervals(ctx).hard, reviewState.passing_review_intervals(ctx).good, reviewState.passing_review_intervals(ctx).easy)
        expected = listOf(2, 3, 4)
        assertEquals(output, expected)

        ctx.fuzz_factor = 0.99f
        output = listOf(reviewState.passing_review_intervals(ctx).hard, reviewState.passing_review_intervals(ctx).good, reviewState.passing_review_intervals(ctx).easy)
        expected = listOf(2, 4, 6)
        assertEquals(output, expected)


        ctx.interval_multiplier = 10.0f
        ctx.maximum_review_interval = 5
        output = listOf(reviewState.passing_review_intervals(ctx).hard, reviewState.passing_review_intervals(ctx).good, reviewState.passing_review_intervals(ctx).easy)
        expected = listOf(5, 5, 5)
        assertEquals(output, expected)
    }

    @Test
    fun low_hard_multiplier_does_not_pull_good_down(){

        val ctx = defaultStateContext()
        ctx.hard_multiplier = 0.1f
        val reviewState = ReviewState(
            scheduled_days = 2,
            elapsed_days = 2,
            ease_factor = 1.3f,
            lapses = 0,
            leeched = false
        )

        ctx.fuzz_factor = 0.0f

        var output = listOf(reviewState.passing_review_intervals(ctx).hard, reviewState.passing_review_intervals(ctx).good, reviewState.passing_review_intervals(ctx).easy)
        var expected = listOf(1, 3, 4)

        assertEquals(output, expected)

    }

    @Test
    fun leech_threshold_met(){
        val reviewState = ReviewState(
            scheduled_days = 1,
            elapsed_days = 1,
            ease_factor = 1.3f,
            lapses = 0,
            leeched = false
        )

        assert(!reviewState.leech_threshold_met(0, 3))
        assert(!reviewState.leech_threshold_met(1, 3))
        assert(!reviewState.leech_threshold_met(2, 3))
        assert(reviewState.leech_threshold_met(3, 3))
        assert(!reviewState.leech_threshold_met(4, 3))
        assert(reviewState.leech_threshold_met(5, 3))
        assert(!reviewState.leech_threshold_met(6, 3))
        assert(reviewState.leech_threshold_met(7, 3))

        assert(!reviewState.leech_threshold_met(7, 8))
        assert(reviewState.leech_threshold_met(8, 8))
        assert(!reviewState.leech_threshold_met(9, 8))
        assert(!reviewState.leech_threshold_met(10, 8))
        assert(!reviewState.leech_threshold_met(11, 8))
        assert(reviewState.leech_threshold_met(12, 8))
        assert(!reviewState.leech_threshold_met(13, 8))

        // 0 means off
        assert(!reviewState.leech_threshold_met(0, 0))

        // no div by zero half of 1 is 1
        assert(!reviewState.leech_threshold_met(0, 1))
        assert(reviewState.leech_threshold_met(1, 1))
        assert(reviewState.leech_threshold_met(2, 1))
        assert(reviewState.leech_threshold_met(3, 1))
    }
}
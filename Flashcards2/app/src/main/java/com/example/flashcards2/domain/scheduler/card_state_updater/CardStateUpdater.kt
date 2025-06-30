package com.example.flashcards2.domain.scheduler.card_state_updater

import com.example.flashcards2.domain.model.Flashcard
import com.example.flashcards2.domain.scheduler.card_state_updater.Constants.PYTHON_UNIT_TESTS
import com.example.flashcards2.domain.scheduler.loadbalancer.LoadBalancerContext
import com.example.flashcards2.domain.scheduler.queue.CardQueue
import com.example.flashcards2.domain.scheduler.states.CardState
import com.example.flashcards2.domain.scheduler.states.LearnState
import com.example.flashcards2.domain.scheduler.states.NewState
import com.example.flashcards2.domain.scheduler.states.RelearnState
import com.example.flashcards2.domain.scheduler.states.ReviewState
import com.example.flashcards2.domain.scheduler.timestamp.SchedTimingToday
import com.example.flashcards2.domain.scheduler.timestamp.TimestampSecs

import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round
import kotlin.random.Random


class CardStateUpdater(
    val flashcard: Flashcard,
    val now: TimestampSecs,
    val timing: SchedTimingToday,
    val fuzz_seed: Long?
) {
    fun current_card_state(): CardState {

        // ensure due time is not before today,
        // which avoids tripping up test_nextIvl() in the Python tests
       val due = if(flashcard.ctype == CardType.Review){
            min((flashcard.due.toLong()), timing.days_elapsed).toInt()
        }else{
           flashcard.due
        }
        return normal_study_state(due)
    }

    fun normal_study_state(due: Int): CardState {
        val interval = flashcard.interval
        val lapses = flashcard.lapses
        val ease_factor = flashcard.ease_factor()
        val remaining_steps = flashcard.remaining_steps()

        when(flashcard.ctype){
           CardType.New -> {
                 return CardState.New(
                     NewState(
                         position = max(due, 0)
                     )
                 )
             }
            CardType.Learn -> {
                //[1.0F, 10.0F] - default value
                val last_ivl = LearningSteps(
                    listOf(1.0F, 10.0F)
                ).current_delay_sec(remaining_steps)
                return CardState.Learning(
                    LearnState(
                        scheduled_secs = last_ivl,
                        remaining_steps = remaining_steps,
                        elapsed_secs = elapsed_secs(last_ivl, due)
                    )
                )
            }
            CardType.Review -> {
                return CardState.Review(
                    ReviewState(
                        scheduled_days = interval,
                        elapsed_days = maxOf((interval - (due - timing.days_elapsed)),0).toInt(),
                        ease_factor = ease_factor,
                        lapses = lapses,
                        leeched = false
                    )
                )
            }
            CardType.Relearn -> {
                //[10.0F] - default value
                val last_ivl = LearningSteps(
                    listOf(10.0F)
                ).current_delay_sec(remaining_steps)

                return CardState.Relearning(
                    RelearnState(
                        LearnState(
                            scheduled_secs = last_ivl,
                            elapsed_secs = elapsed_secs(last_ivl, due),
                            remaining_steps = remaining_steps
                        ),
                        ReviewState(
                            scheduled_days = interval,
                            elapsed_days= interval,
                            ease_factor = ease_factor,
                            lapses =  lapses,
                            leeched = false
                        )
                    )
                )
            }
        }
       
    }

    /*fun elapsed_secs (last_ivl: Int, due: Int): Int {
        return when (card.queue) {
            CardQueue.Learn -> {
                // Decrease reps by 1 to get correct seed for fuzz.
                // If the fuzz calculation changes, this will break.
                val last_ivl_with_fuzz = learning_ivl_with_fuzz(
                    get_fuzz_seed_for_id_and_reps(card.id, (card.reps - 1)),
                    last_ivl
                )
                val last_answered_time  = due.toLong() - last_ivl_with_fuzz .toLong()

                return (now.value - last_answered_time).toInt()
            }
            CardQueue.DayLearn -> {
                val days_since_col_creation = timing.days_elapsed.toInt()
                // Need .max(1) for same day learning cards pushed to the next day.
                // 86400 is the number of seconds in a day.
                val last_ivl_as_days  = (last_ivl  / 86400).coerceAtLeast(1).toInt()
                val elapsed_days  = days_since_col_creation - due + last_ivl_as_days


                return (elapsed_days * 86400).toInt()
            }
            else -> 0 // Not used for other card queues.
        }
    }*/

    fun elapsed_secs (last_ivl: Int, due: Int): Int {
         when (flashcard.queue) {
            CardQueue.Learn -> {
                // Decrease reps by 1 to get correct seed for fuzz.
                // If the fuzz calculation changes, this will break.
                val seed = get_fuzz_seed_for_id_and_reps(flashcard.id!!, flashcard.reps - 1)
                val lastIvlWithFuzz = learning_ivl_with_fuzz(seed, last_ivl)
                val lastAnsweredTime = due.toLong() - lastIvlWithFuzz.toLong()

                return (now.value - lastAnsweredTime).toInt()
            }

            CardQueue.DayLearn -> {
                val daysSinceColCreation = timing.days_elapsed
                // Need .coerceAtLeast(1) for same day learning cards pushed to the next day.
                // 86_400 is the number of seconds in a day.
                val lastIvlAsDays = (last_ivl / 86400).coerceAtLeast(1)
                val elapsedDays = daysSinceColCreation - due + lastIvlAsDays

                return elapsedDays.toInt() * 86400
            }

            else -> return 0 // Not used for other card queues.
        }
    }


   fun get_fuzz_factor(seed: Long?): Float{
       return seed?.let { Random(it).nextDouble(0.0, 1.0) }!!.toFloat()
   }

    /// If in test environment, disable fuzzing.
    private fun get_fuzz_seed_for_id_and_reps(card_id: Long, card_reps: Int): Long? {
        return if (PYTHON_UNIT_TESTS) {
            null
        } else {
            card_id.plus(card_reps.toLong())
        }
    }


    fun learning_ivl_with_fuzz(inputSeed: Long?, secs: Int): Int{
        return if (inputSeed != null) {
            val rng = Random(inputSeed)
            val upperExclusive = secs + floor((secs.toFloat() * 0.25f).coerceAtMost(300f)).toInt()
            if (secs >= upperExclusive) {
                secs
            } else {
                rng.nextInt(secs, upperExclusive)
            }
        } else {
            secs
        }
    }

    // Return the next states that will be applied for each answer button.
   // https://github.com/glutanimate/anki/blob/main/rslib/src/scheduler/answering/mod.rs#L228

    //StateContext default config
    //https://github.com/glutanimate/anki/blob/main/rslib/src/scheduler/answering/mod.rs#L228

    // DEFAULT_DECK_CONFIG_INNER
    //https://github.com/glutanimate/anki/blob/72abb7ec5bfdb0e00029f721fc5f5cf13b5085a4/rslib/src/deckconfig/mod.rs#L32
    fun get_scheduling_states(): SchedulingStates {
        val current = current_card_state()
        val state_ctx =
            StateContext(
                fuzz_factor = get_fuzz_factor(fuzz_seed),
                steps = LearningSteps(
                    listOf(1.0f, 10.0f)
                ),
                graduating_interval_good = 1,
                graduating_interval_easy = 4,
                initial_ease_factor = 2.5F,
                hard_multiplier = 1.2F,
                easy_multiplier = 1.3F,
                interval_multiplier = 1.0F,
                maximum_review_interval = 36500,
                leech_threshold = 8,
                relearn_steps = LearningSteps(
                    listOf(10.0F)
                ),
                lapse_multiplier = 0.0F,
                minimum_lapse_interval = 1,
                in_filtered_deck = false,
                load_balancer_ctx = getLoadBalancer(),
                preview_delays = PreviewDelays(
                    again = 1,
                    hard = 10,
                    good = 0
                )
            )

        /* let load_balancer_ctx = self.state.card_queues.as_ref().and_then(|card_queues| {
            match card_queues.load_balancer.as_ref() {
                None => None,
                Some(load_balancer) => {
                    Some(load_balancer.review_context(note_id, deck.config_id()?))
                }
            }
        });

        let state_ctx = ctx.state_context(load_balancer_ctx);*/

        return current.next_states(state_ctx)
    }

    private fun getLoadBalancer(): LoadBalancerContext? {
        return null
    }


    fun apply_review_state(
        current: CardState,
        next: ReviewState
    ){
        flashcard.queue = CardQueue.Review
        flashcard.ctype =
            CardType.Review
        flashcard.interval = next.scheduled_days
        flashcard.due = (timing.days_elapsed + next.scheduled_days).toInt()
        flashcard.ease_factor = round(next.ease_factor * 1000.0f).toInt()
        flashcard.lapses = next.lapses
        flashcard.remaining_steps = 0

        current.new_position()?.let { position ->
            flashcard.original_position = position
        }

    }

    fun apply_new_state(
        current: CardState,
        next: NewState
    ){
        flashcard.ctype =
            CardType.New
        flashcard.queue = CardQueue.New
        flashcard.due = next.position
        flashcard.original_position = null
    }

    fun apply_learning_state(
        current: CardState,
        next: LearnState,
    ){
        flashcard.remaining_steps = next.remaining_steps
        flashcard.ctype =
            CardType.Learn

        current.new_position()?.let { position ->
            flashcard.original_position = position
        }

        val interval = next
            .interval_kind()
            .maybe_as_days(secs_until_rollover())

        when(interval){
            is IntervalKind.InSecs -> {
                flashcard.queue = CardQueue.Learn
                flashcard.due = fuzzed_next_learning_timestamp(interval.secs)
            }

            is IntervalKind.InDays -> {
                flashcard.queue = CardQueue.DayLearn
                flashcard.due = (timing.days_elapsed + interval.days).toInt()
            }
        }
    }

    fun apply_relearning_state(
        current: CardState,
        next: RelearnState,
        ){
        flashcard.interval = next.review.scheduled_days
        flashcard.remaining_steps = next.learning.remaining_steps
        flashcard.ctype =
            CardType.Relearn
        flashcard.lapses = next.review.lapses
        flashcard.ease_factor = round(next.review.ease_factor * 1000.0f).toInt()

        current.new_position()?.let { position ->
            flashcard.original_position = position
        }

        val interval = next
            .interval_kind()
            .maybe_as_days(secs_until_rollover())

        when(interval){
            is IntervalKind.InSecs -> {
                flashcard.queue = CardQueue.Learn
                flashcard.due = fuzzed_next_learning_timestamp(interval.secs)
            }

            is IntervalKind.InDays -> {
                flashcard.queue = CardQueue.DayLearn
                flashcard.due = (timing.days_elapsed + interval.days).toInt()
            }
        }
    }

    fun secs_until_rollover(): Int{
        return IntervalCalculatorHelper()
            .next_day_at_secs_since_now().toInt()
    }

    fun fuzzed_next_learning_timestamp(secs: Int): Int{
        return TimestampSecs().now().value.toInt() + learning_ivl_with_fuzz(fuzz_seed, secs)
    }
}
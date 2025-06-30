package com.example.flashcards2.domain.scheduler.card_state_updater

import com.example.flashcards2.domain.scheduler.card_state_updater.Constants.FUZZ_RANGES
import com.example.flashcards2.domain.scheduler.loadbalancer.LoadBalancerContext
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

class StateContext(
    var fuzz_factor: Float?,
    var steps: LearningSteps,
    var graduating_interval_good: Int,
    var graduating_interval_easy: Int,
    var initial_ease_factor: Float,
    var hard_multiplier: Float,
    var easy_multiplier: Float,
    var interval_multiplier: Float,
    var maximum_review_interval: Int,
    var leech_threshold: Int =  8,
    var relearn_steps: LearningSteps,
    var lapse_multiplier: Float,
    var minimum_lapse_interval: Int,
    var in_filtered_deck: Boolean,
    var load_balancer_ctx: LoadBalancerContext?,
    var preview_delays: PreviewDelays
) {

    fun min_and_max_review_intervals(minimum: Int): Pair<Int, Int> {
        val maximum = maximum_review_interval.coerceAtLeast(1)
        val minimum = minimum.coerceIn(1, maximum)
        return Pair(minimum, maximum)
    }

    //https://github.com/glutanimate/anki/blob/main/rslib/src/scheduler/states/fuzz.rs#L36
    fun with_review_fuzz( interval: Float, minimum: Int, maximum: Int): Int{
         return load_balancer_ctx?.find_interval(interval, minimum, maximum)
            ?: with_review_fuzz(fuzz_factor, interval, minimum, maximum)
       // return with_review_fuzz(fuzz_factor, interval, minimum, maximum)
    }

    fun with_review_fuzz(fuzz_factor: Float?, interval: Float, minimum: Int, maximum: Int): Int{

        if(fuzz_factor != null){
            val constraints = constrained_fuzz_bounds(interval, minimum, maximum)
            val lower = constraints[0]
            val upper = constraints[1]

            return  floor(lower.toFloat() + fuzz_factor * ((1 + upper - lower).toFloat())).toInt()

        }else{

            return round(interval).toInt().coerceIn(minimum, maximum)
        }
    }


    fun constrained_fuzz_bounds(_interval: Float, _minimum: Int, _maximum: Int): List<Int> {
        val minimum = min(_minimum, _maximum)
        val interval = _interval.coerceIn(minimum.toFloat(), _maximum.toFloat())

        val bounds = fuzz_bounds(interval)
        var lower = bounds[0]
        var upper = bounds[1]

        lower = lower.coerceIn(minimum, _maximum)
        upper = upper.coerceIn(minimum, _maximum)

        if (upper == lower && upper > 2 && upper < _maximum) {
            upper = lower + 1;
        }

        return listOf(lower, upper)
    }

    fun fuzz_bounds(interval: Float): List<Int>{
        val delta = fuzz_delta(interval)

        val lower = round(interval - delta).toInt()
        val upper = round(interval + delta).toInt()

        return listOf(lower, upper)
    }

    fun fuzz_delta(interval: Float): Float{

        if (interval < 2.5F) {
            return 0.0F
        }else{
            var delta = 1.0F
            FUZZ_RANGES.forEach { range ->
                delta += range.factor * max((min(interval, range.end) - range.start),0.0F)
            }

            return delta
        }
    }
}
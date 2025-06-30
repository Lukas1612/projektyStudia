package com.example.flashcards2.domain.scheduler.loadbalancer


import com.example.flashcards2.domain.scheduler.card_state_updater.Constants.FUZZ_RANGES
import com.example.flashcards2.domain.scheduler.card_state_updater.Constants.MAX_LOAD_BALANCE_INTERVAL
import com.example.flashcards2.domain.scheduler.card_state_updater.EasyDay
import com.example.flashcards2.domain.scheduler.timestamp.TimestampSecs
import java.time.DayOfWeek
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random

class LoadBalancer(
    val days_by_preset: MutableMap<Long, Array<LoadBalancerDay>>,
    val next_day_at: TimestampSecs,
    val easy_days_percentages_by_preset: Map<Long, Array<EasyDay>>
) {
    fun find_interval(
        interval: Float,
        minimum: Int,
        maximum: Int,
        deckconfig_id: Long,
        fuzz_seed: Long?
    ): Int? {

        if(interval > MAX_LOAD_BALANCE_INTERVAL
            || minimum > MAX_LOAD_BALANCE_INTERVAL)
        {
            return null
        }
        val (before_days, after_days) = constrained_fuzz_bounds(interval, minimum, maximum)

        val days = days_by_preset[deckconfig_id]
        val interval_days = days?.slice(before_days ..after_days)

        val (review_counts, weekdays) = interval_days
            ?.mapIndexed { i, day ->
                Pair(
                    day.cards.size,
                    interval_to_weekday(i + before_days, next_day_at)
                )
            }!!
            .unzip()

        val easy_days_load = easy_days_percentages_by_preset[deckconfig_id] ?: return null
        val easy_days_modifier =
        calculate_easy_days_modifiers(easy_days_load, weekdays, review_counts)

        val intervals = interval_days.mapIndexed { interval_index, interval_day ->

            LoadBalancerInterval(
                target_interval = (interval_index + before_days),
                review_count = review_counts[interval_index],
                easy_days_modifier = easy_days_modifier[interval_index]
            )
        }

       return select_weighted_interval(intervals, fuzz_seed)
    }


    fun select_weighted_interval(
        intervals: Iterable<LoadBalancerInterval>,
        fuzz_seed: Long?
    ): Int? {
        val intervals_and_weights = intervals.map { interval ->
            val weight = when (interval.review_count) {
                0 -> 1.0f // if there are no cards due on this day, give it the full 1.0 weight
                else -> {
                    val cardCountWeight = (1.0f / interval.review_count).pow(2.15f)
                    val cardIntervalWeight = (1.0f / interval.target_interval).pow(3)

                    cardCountWeight *
                            cardIntervalWeight *
                            interval.easy_days_modifier
                }
            }
            Pair(interval.target_interval, weight)
        }.toList()

        if (intervals_and_weights.isEmpty()) {
            return null
        }

        val rng = fuzz_seed?.let { Random(it) } ?: Random.Default

        val weights = intervals_and_weights.map { it.second }
        val weightedIndex = WeightedIndex(weights)

        val selectedIntervalIndex = weightedIndex.getRandomIndex(rng)
        return intervals_and_weights.getOrNull(selectedIntervalIndex)?.first
    }

    class WeightedIndex(weights: List<Float>) {
        private val cumulativeWeights: List<Float> = weights
            .scan(0.0f) { acc, weight -> acc + weight }
            .drop(1) // Remove the initial 0

        private val totalWeight = cumulativeWeights.lastOrNull() ?: 0.0f

        fun getRandomIndex(random: Random = Random.Default): Int {
            if (totalWeight <= 0.0f) {
                return -1 // Or handle the case where all weights are zero or negative
            }
            val randomValue = random.nextFloat() * totalWeight
            return cumulativeWeights.indexOfFirst { it > randomValue }
        }
    }


    fun calculate_easy_days_modifiers(
        easy_days_load: Array<EasyDay>,
        weekdays: List<Int>,
        review_counts: List<Int>
    ): List<Float>{
        val totalReviewCount = review_counts.sum()
        val totalPercents = weekdays.sumOf { easy_days_load[it].load_modifier().toDouble() }.toFloat()

        return weekdays.zip(review_counts).map { (weekday, reviewCount) ->
            val day = when (easy_days_load[weekday]) {
                EasyDay.Reduced -> {
                    val half = 0.5f
                    val otherDaysReviewTotal = (totalReviewCount - reviewCount).toFloat()
                    val otherDaysPercentTotal = totalPercents - half
                    val normalizedCount = reviewCount / half
                    val reducedDayThreshold = otherDaysReviewTotal / otherDaysPercentTotal

                    if (normalizedCount > reducedDayThreshold) {
                        EasyDay.Minimum
                    } else {
                        EasyDay.Normal
                    }
                }
                else -> easy_days_load[weekday]
            }
            day.load_modifier()
        }
    }

    fun interval_to_weekday(interval: Int, next_day_at: TimestampSecs): Int{
        val target_datetime  = next_day_at
            .adding_secs((interval - 1).toLong() * 86400)
            .local_datetime()
            .getOrThrow()

        return target_datetime.dayOfWeek.value - DayOfWeek.MONDAY.value
    }

    fun constrained_fuzz_bounds(_interval: Float, minimum: Int, maximum: Int): Pair<Int, Int> {
        val minimum = min(minimum, maximum)
        val interval = _interval.coerceIn(minimum.toFloat(), maximum.toFloat())
        val bounds = fuzz_bounds(interval)
        var lower = bounds.first
        var upper = bounds.second

        lower = lower.coerceIn(minimum, maximum)
        upper = upper.coerceIn(minimum, maximum)

        if (upper == lower && upper > 2 && upper < maximum) {
            upper = lower + 1
        }

        return Pair(lower, upper)
    }

    fun fuzz_bounds(interval: Float): Pair<Int, Int> {
        val delta = fuzz_delta(interval)
        return Pair(
            (interval - delta).roundToInt(),
            (interval + delta).roundToInt()
        )
    }

    fun fuzz_delta(interval: Float): Float{
        return if (interval < 2.5f) {
            0.0f
        } else {
            var delta = 1.0f
            FUZZ_RANGES.forEach { range ->
                delta += range.factor * max(0.0f,(min(range.end, interval) - range.start))
            }

            return delta
        }
    }

    fun review_context(
        note_id: Int?,
        deckconfig_id: Long
    ): LoadBalancerContext {
        return LoadBalancerContext(
            load_balancer = this,
            note_id = note_id,
            deckconfig_id = deckconfig_id,
            fuzz_seed = null
            )
    }
}



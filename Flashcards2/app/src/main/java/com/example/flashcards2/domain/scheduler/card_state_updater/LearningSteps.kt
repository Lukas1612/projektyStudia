package com.example.flashcards2.domain.scheduler.card_state_updater

import com.example.flashcards2.domain.scheduler.card_state_updater.Constants.DAY
import kotlin.math.min
import kotlin.math.roundToInt

class LearningSteps(
    // The steps in minutes
    val steps: List<Float>
) {
    fun to_secs(v: Float): Int{
        return (v * 60.0).toInt()
    }

    fun again_delay_secs_relearn(): Int? {

        return if(steps.isEmpty()){
            null
        }else {
            secs_at_index(0)
        }

    }

    fun again_delay_secs_learn(): Int?{
        return secs_at_index(0)
    }

    fun secs_at_index(index: Int): Int?{
        return steps.getOrNull(index)?.let {
            to_secs(it)
        }
    }

    fun remaining_for_failed(): Int {
        return steps.size
    }

    fun current_delay_sec(remaining: Int): Int{
        val idx = get_index(remaining)
        return secs_at_index(idx) ?: 0
    }

    fun get_index(remaining: Int): Int {
        val total = steps.size
        val subtracted = total - (remaining % 1000).coerceAtMost(total)
        return subtracted.coerceAtMost(total - 1).coerceAtLeast(0)
    }

    fun hard_delay_secs(remaining: Int): Int?{
        val idx = get_index(remaining)

        val current = secs_at_index(idx) ?: steps.firstOrNull()?.let { to_secs(it) } ?: return null
        return if (idx == 0) hard_delay_secs_for_first_step(current) else current
    }

    fun good_delay_secs(remaining: Int): Int?{
        val idx = get_index(remaining)
        return secs_at_index(idx + 1)
    }

    fun remaining_for_good(remaining: Int): Int{
        val idx = get_index(remaining)
        return steps.size.saturating_sub(idx + 1)
    }

    fun hard_delay_secs_for_first_step(again_secs: Int): Int{

        val next = secs_at_index(1)
        return if(next != null){
            maybe_round_in_days(again_secs.saturating_add(next) / 2)
        }else{
            val secs = min((again_secs.saturating_mul(3) / 2), (again_secs.saturating_add(DAY)))
            maybe_round_in_days(secs)
        }
    }

    fun maybe_round_in_days(secs: Int): Int{
        return if(secs > DAY){
            (secs.toFloat()/DAY.toFloat()).roundToInt().saturating_mul(DAY)
        }else{
            secs
        }
    }

    fun Int.saturating_mul(other: Int): Int {
        val result = this.toLong() * other.toLong()
        return result.coerceIn(Int.MIN_VALUE.toLong(), Int.MAX_VALUE.toLong()).toInt()
    }

    fun Int.saturating_add(other: Int): Int{
        val result = this.toLong() + other.toLong()
        return result.coerceIn(Int.MIN_VALUE.toLong(), Int.MAX_VALUE.toLong()).toInt()
    }

    fun Int.saturating_sub(other: Int): Int {
        val result = this.toLong() - other.toLong()
        return result.coerceIn(Int.MIN_VALUE.toLong(), Int.MAX_VALUE.toLong()).toInt()
    }
}
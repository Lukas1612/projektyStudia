package com.example.flashcards2.domain.scheduler.card_state_updater

import com.example.flashcards2.domain.scheduler.card_state_updater.Constants.LEARN_AHEAD_SECS
import com.example.flashcards2.domain.scheduler.timestamp.Timespan
import com.example.flashcards2.domain.scheduler.timestamp.TimespanUnit

import kotlin.math.max


class AnswersIntervalsStringBuilder {

    // Describe the next intervals, to display on the answer buttons.
    fun describe_next_states(choices: SchedulingStates): List<String>{
        val collapse_time = LEARN_AHEAD_SECS
        val secs_until_rollover = max(
            IntervalCalculatorHelper().next_day_at_secs_since_now(), 0).toInt() //******

        return listOf(
            answer_button_time_collapsible(
                choices
                    .again
                    .interval_kind()
                    .maybe_as_days(secs_until_rollover)
                    .as_seconds(),
                collapse_time
            )+ " again",
            answer_button_time_collapsible(
                choices
                    .hard
                    .interval_kind()
                    .maybe_as_days(secs_until_rollover)
                    .as_seconds(),
                collapse_time
            )+ " hard",
            answer_button_time_collapsible(
                choices
                    .good
                    .interval_kind()
                    .maybe_as_days(secs_until_rollover)
                    .as_seconds(),
                collapse_time
            ) + " good",
            answer_button_time_collapsible(
                choices
                    .easy
                    .interval_kind()
                    .maybe_as_days(secs_until_rollover)
                    .as_seconds(),
                collapse_time
            ) + " easy",
        )
    }

    // Short string like '4d' to place above answer buttons.
    // Times within the collapse time are represented like '<10m'
   private fun answer_button_time_collapsible(seconds: Int, collapse_secs: Long): String{
        val string = answer_button_time(seconds.toFloat())

        return if(seconds == 0){
            "N"
        }else if(seconds < collapse_secs){
            "<$string"
        }else{
            string
        }
   }

    // Short string like '4d' to place above answer buttons.
   private fun answer_button_time(seconds: Float): String{
        val span = Timespan(seconds).natural_span()
        val amount = span.as_rounded_unit_for_answer_buttons()

        return when(span.unit){
            TimespanUnit.Seconds -> {
                 "" + amount + "s"
            }

            TimespanUnit.Minutes -> {
                 "" + amount + "m"
            }

            TimespanUnit.Hours -> {
                 "" + amount + "h"
            }

            TimespanUnit.Days -> {
                 "" + amount + "d"
            }

            TimespanUnit.Months -> {
                 "" + amount + "mo"
            }

            TimespanUnit.Years -> {
                 "" + amount + "y"
            }
        }
    }

}
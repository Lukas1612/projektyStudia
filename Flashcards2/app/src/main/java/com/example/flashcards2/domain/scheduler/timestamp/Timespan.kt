package com.example.flashcards2.domain.scheduler.timestamp

import kotlin.math.abs
import kotlin.math.round

class Timespan(
    var seconds: Float,
) {
    var unit: TimespanUnit = TimespanUnit.Seconds

    val SECOND = 1.0f
    val MINUTE = 60.0f * SECOND
    val HOUR  = 60.0f * MINUTE
    val DAY  = 24.0f * HOUR
    val MONTH  = 30.417f * DAY // 365/12 ≈ 30.417
    val YEAR = 365.0f * DAY

   fun natural_span(): Timespan {
       val secs = abs(seconds)

       val unit = if(secs < MINUTE){
           TimespanUnit.Seconds
       }else if(secs < HOUR){
           TimespanUnit.Minutes
       }else if(secs < DAY){
           TimespanUnit.Hours
       }else if(secs < MONTH){
           TimespanUnit.Days
       }else if(secs < YEAR){
           TimespanUnit.Months
       }else{
           TimespanUnit.Years
       }

       this.unit = unit

       return this
   }



    fun from_secs(seconds: Float){
        this.seconds = seconds
        unit = TimespanUnit.Seconds
    }

    fun as_str(): String{
        return when(unit){
            TimespanUnit.Seconds -> {
                "seconds"
            }

            TimespanUnit.Minutes -> {
                "minutes"
            }

            TimespanUnit.Hours -> {
                "hours"
            }

            TimespanUnit.Days -> {
                "days"
            }

            TimespanUnit.Months -> {
                "months"
            }

            TimespanUnit.Years -> {
                "years"
            }
        }
    }

    fun as_rounded_unit_for_answer_buttons(): Float{

        return when(unit){
            TimespanUnit.Seconds, TimespanUnit.Minutes, TimespanUnit.Days -> {
               round(as_unit())
            }

            else -> {
                round(as_unit() * 10.0f) / 10.0f
            }
        }
    }

    fun as_unit(): Float{
        val s = seconds

        return when(unit){
            TimespanUnit.Seconds -> {
                s
            }

            TimespanUnit.Minutes -> {
                s / MINUTE
            }

            TimespanUnit.Hours -> {
                s / HOUR
            }

            TimespanUnit.Days -> {
                s / DAY
            }

            TimespanUnit.Months -> {
                s / MONTH
            }

            TimespanUnit.Years -> {
                s / YEAR
            }
        }
    }

}
package com.example.flashcards2.domain.scheduler.card_state_updater


object Constants {
    //********ReviewState************
    const val INITIAL_EASE_FACTOR = 2.5F
    const val MINIMUM_EASE_FACTOR = 1.3F
    const val EASE_FACTOR_AGAIN_DELTA= -0.2F
    const val EASE_FACTOR_HARD_DELTA = -0.15F
    const val EASE_FACTOR_EASY_DELTA  = 0.15F
    //*******************************

    //********StateContext***********
    const val DEFAULT_SECS_IF_MISSING: Int = 60
    const val DAY: Int = 60 * 60 * 24
    const val LEARN_AHEAD_SECS = 1200L // 20 minutes = 60s*20
    //*******************************
    const val MAX_LOAD_BALANCE_INTERVAL: Int = 90
    const val LOAD_BALANCE_DAYS: Int = (MAX_LOAD_BALANCE_INTERVAL.toFloat() * 1.1).toInt()
    const val SIBLING_PENALTY: Float = 0.001f

    val FUZZ_RANGES = listOf(
        FuzzRange(
            start = 2.5F,
            end = 7.0F,
            factor = 0.15F
        ),
        FuzzRange(
            start = 7.0F,
            end = 20.0F,
            factor = 0.1F
        )
        ,
        FuzzRange(
            start = 20.0F,
            end = Float.MAX_VALUE,
            factor = 0.05F
        )
    )

    const val PYTHON_UNIT_TESTS = false

}
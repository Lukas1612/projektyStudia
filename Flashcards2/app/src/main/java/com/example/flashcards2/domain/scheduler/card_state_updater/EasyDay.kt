package com.example.flashcards2.domain.scheduler.card_state_updater

enum class EasyDay {
    Minimum,
    Reduced,
    Normal;

    fun from(value: Float): EasyDay {
        return when (value) {
            1.0f -> Normal
            0.0f -> Minimum
            else -> Reduced
        }
    }

    fun load_modifier(): Float{
        return when(this){
            Minimum -> {
                0.0001f
            }

            Reduced -> {
                0.5f
            }
            Normal -> {
                1.0f
            }
        }
    }
}
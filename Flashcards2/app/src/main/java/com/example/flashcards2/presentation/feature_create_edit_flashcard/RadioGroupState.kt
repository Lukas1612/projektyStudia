package com.example.flashcards2.presentation.feature_create_edit_flashcard

data class RadioGroupState(
    val buttonsState: List<Boolean> = listOf(
        false, //button_0
        false, //button_1
        false, //button_2
        false, //button_3
        false, //button_4
        false  //button_5
    )
)
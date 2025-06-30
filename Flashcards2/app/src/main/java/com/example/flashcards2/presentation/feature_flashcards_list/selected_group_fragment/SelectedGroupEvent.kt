package com.example.flashcards2.presentation.feature_flashcards_list.selected_group_fragment

sealed class SelectedGroupEvent {
    object EditButtonClicked: SelectedGroupEvent()
    object DeleteButtonClicked: SelectedGroupEvent()
    object DeleteButtonConfirmed: SelectedGroupEvent()
    object LearnButtonClicked: SelectedGroupEvent()
}
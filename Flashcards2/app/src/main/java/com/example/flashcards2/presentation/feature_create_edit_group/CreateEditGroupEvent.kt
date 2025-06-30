package com.example.flashcards2.presentation.feature_create_edit_group

sealed class CreateEditGroupEvent {
    data class SaveGroup(val groupName: String) : CreateEditGroupEvent()
}
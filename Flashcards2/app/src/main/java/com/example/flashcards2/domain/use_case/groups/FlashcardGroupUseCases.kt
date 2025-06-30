package com.example.flashcards2.domain.use_case.groups

data class FlashcardGroupUseCases(
    val addFlashcardGroup: AddFlashcardGroup,
    val deleteFlashcardGroup: DeleteFlashcardGroup,
    val getFlashcardGroupById: GetFlashcardGroupById,
    val getFlashcardGroupByName: GetFlashcardGroupByName,
    val getFlashcardGroups: GetFlashcardGroups,
    val deleteFlashcardGroupById: DeleteFlashcardGroupById
)
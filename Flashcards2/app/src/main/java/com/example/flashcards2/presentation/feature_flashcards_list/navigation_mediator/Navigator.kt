package com.example.flashcards2.presentation.feature_flashcards_list.navigation_mediator

interface Navigator {

    fun setListener(_navigationListener: NavigationListener){
    }

    fun navigate(groupId: Long)
    {
    }
}
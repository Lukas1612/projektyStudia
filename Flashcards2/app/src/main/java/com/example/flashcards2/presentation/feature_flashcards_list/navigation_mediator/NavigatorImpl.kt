package com.example.flashcards2.presentation.feature_flashcards_list.navigation_mediator

class NavigatorImpl: Navigator {
    private var navigationListener: NavigationListener? = null

    override fun setListener(_navigationListener: NavigationListener){
        navigationListener = _navigationListener
    }

    override fun navigate(groupId: Long)
    {
        navigationListener?.navigateToFlashcardList(groupId)
    }
}
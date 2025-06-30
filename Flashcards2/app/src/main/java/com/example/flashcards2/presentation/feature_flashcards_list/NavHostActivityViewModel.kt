package com.example.flashcards2.presentation.feature_flashcards_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavHostActivityViewModel @Inject constructor(): ViewModel() {

    private val _state = MutableStateFlow(NavHostActivityState())
    val state: StateFlow<NavHostActivityState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    init {
      /*  navigator.setListener(object : NavigationListener{
            override fun navigateToFlashcardList(groupId: Long) {
               viewModelScope.launch {
                   _eventFlow.emit(
                       UiEvent.GoToFlashcardListFragment(groupId)
                   )
               }
            }
        })*/
    }

    fun onEvent(event: NavHostActivityEvent){
        when(event)
        {
            is NavHostActivityEvent.GoToFlashcardListFragment -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        UiEvent.GoToFlashcardListFragment(event.groupId)
                    )
                }
            }
        }
    }

    sealed class UiEvent{
        data class GoToFlashcardListFragment(val groupId: Long) : UiEvent()
    }
}
package com.example.flashcards2.presentation.feature_flashcards_list.selected_group_fragment

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards2.domain.scheduler.card_state_updater.Constants.LEARN_AHEAD_SECS
import com.example.flashcards2.domain.scheduler.queue.CardQueues
import com.example.flashcards2.domain.scheduler.queue.Counts
import com.example.flashcards2.domain.scheduler.queue.QueueBuilder
import com.example.flashcards2.domain.use_case.col.ColUseCases
import com.example.flashcards2.domain.use_case.flashcard.FlashcardUseCases
import com.example.flashcards2.domain.use_case.groups.FlashcardGroupUseCases
import com.example.flashcards2.presentation.Constants.GROUP_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectedGroupFragmentViewModel @Inject constructor(
    private val flashcardUseCases: FlashcardUseCases,
    private val flascardGroupUseCases: FlashcardGroupUseCases,
    private val colUseCases: ColUseCases,
    val savedStateHandle: SavedStateHandle
): ViewModel(){


    private val _state = MutableStateFlow(SelectedGroupState())
    val state: StateFlow<SelectedGroupState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private lateinit var cardQueues: CardQueues

    private var groupId: Long? = null

    init {
        viewModelScope.launch {
            val crt = colUseCases.getCol().first()[0].creationTimestampSeconds

            savedStateHandle.get<Long>(GROUP_ID_KEY)?.let { id ->

                Log.d("init sel", "groupId $id")
                cardQueues = QueueBuilder(flashcardUseCases).build(LEARN_AHEAD_SECS, crt, id)
                groupId = id

                initState(cardQueues)

            } ?: throw IllegalArgumentException("GROUP_ID_KEY is missing in SavedStateHandle")
        }
    }

    fun onEvent(event: SelectedGroupEvent){

        when(event){
            is SelectedGroupEvent.EditButtonClicked -> {
                openEditGroupScreen()
            }

           is SelectedGroupEvent.DeleteButtonClicked -> {
               openDeletedItemsAlert()
            }

            is SelectedGroupEvent.DeleteButtonConfirmed -> {
                viewModelScope.launch {
                    deleteGroup()
                    goBackToFlashcardGroupsScreen()
                }
            }

            is SelectedGroupEvent.LearnButtonClicked -> {
                openLearningScreen()
            }
        }
    }

    private suspend fun deleteGroup(){
        flashcardUseCases.deleteFlashcardsByGroupId(groupId!!)
        flascardGroupUseCases.deleteFlashcardGroupById(groupId!!)
    }

    private fun openEditGroupScreen(){
        viewModelScope.launch {
            _eventFlow.emit(
                UiEvent.OpenEditGroupScreen
            )
        }
    }

    private fun openLearningScreen(){
        viewModelScope.launch {
            _eventFlow.emit(
                UiEvent.OpenLearningScreen
            )
        }
    }



    private fun initState(cardQueues: CardQueues){
        if(cardQueues.counts.isNonZero()){
            setLearnButtonVisible()
            theNumberOfCardsToLearnText(cardQueues.counts)
        }else{
            zeroCardsToLearnText()
        }
    }

    private fun setLearnButtonVisible() {
        _state.update { previousView ->
            previousView.copy(
                learnButtonVisibility = true
            )
        }
    }

    private fun goBackToFlashcardGroupsScreen(){
        viewModelScope.launch {
            _eventFlow.emit(
                UiEvent.GoBackToFlashcardGroupsScreen
            )
        }

    }

    private fun theNumberOfCardsToLearnText(counts: Counts) {

        val string = prepareString(counts)

        _state.update { previousView ->
            previousView.copy(
                numberOfCardsToReviewText = string
            )
        }

    }

    private fun prepareString(counts: Counts): String {
        val new = counts.new.toString()
        val review = counts.review.toString()
        val learning = counts.learning.toString()

        return "new: $new\n" +
                "review: $review\n" +
                "learning: $learning\n"
    }

    private fun zeroCardsToLearnText() {
        _state.update { previousView ->
            previousView.copy(
                numberOfCardsToReviewText = "there are no cards to learn"
            )
        }
    }

    private fun openDeletedItemsAlert(){
        viewModelScope.launch {
            _eventFlow.emit(
                UiEvent.OpenDeletedItemsAlert
            )
        }
    }

    fun getGroupId(): Long? {
        return groupId
    }

    sealed class UiEvent{
        object OpenEditGroupScreen: UiEvent()
        object GoBackToFlashcardGroupsScreen: UiEvent()
        object OpenLearningScreen: UiEvent()
        object OpenDeletedItemsAlert: UiEvent()
    }
}
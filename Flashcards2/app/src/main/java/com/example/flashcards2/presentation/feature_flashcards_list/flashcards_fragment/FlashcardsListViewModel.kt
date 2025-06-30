package com.example.flashcards2.presentation.feature_flashcards_list.flashcards_fragment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards2.domain.model.Flashcard
import com.example.flashcards2.domain.use_case.flashcard.FlashcardUseCases
import com.example.flashcards2.presentation.Constants.GROUP_ID_KEY
import com.example.flashcards2.presentation.Constants.NULL_FLASHCARD_ID
import com.example.flashcards2.presentation.feature_flashcards_list.flashcards_fragment.adapters.FlashcardListAdapterItem
import com.example.flashcards2.presentation.feature_flashcards_list.helpers.SelectionBehaviourHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlashcardsListViewModel @Inject constructor(
    private val flashcardUseCases: FlashcardUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(FlashcardsListState())
    val state: StateFlow<FlashcardsListState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var selectionBehaviourHelper: SelectionBehaviourHelper<FlashcardListAdapterItem>


    private var getFlashcardsJob: Job? = null

    init {
        selectionBehaviourHelper = object : SelectionBehaviourHelper<FlashcardListAdapterItem>(){
            override fun updateViewData(items: List<FlashcardListAdapterItem>) {
                updateStateData(items)
            }

            override fun doOnToolBarVisibilityChanged(isToolBarVisible: Boolean) {
                setToolBarVisibility(isToolBarVisible)
            }

            override fun createUnelectedItemFromItem(item: FlashcardListAdapterItem): FlashcardListAdapterItem {
                return FlashcardListAdapterItem(
                    flashcard = item.flashcard,
                    isSelected = false
                )
            }

            override fun createSelectedItemFromItem(item: FlashcardListAdapterItem): FlashcardListAdapterItem {
                return FlashcardListAdapterItem(
                    flashcard = item.flashcard,
                    isSelected = true
                )
            }

            override fun getItemId(item: FlashcardListAdapterItem): Long {
                return item.flashcard.id!!
            }

        }

        savedStateHandle.get<Long>(GROUP_ID_KEY)?.let { groupId ->

            updateStateGroupId(groupId)

             getFlashcards()
        }
    }

    private fun getFlashcards()
    {
        getFlashcardsJob?.cancel()
        getFlashcardsJob = flashcardUseCases.getFlashcardsByGroupId(_state.value.groupId!!)
            .onEach { updatedList ->

                updateStateData(updatedList.map { flashcardToItem(it) })

            }.launchIn(viewModelScope)
    }

    fun onEvent(event: FragmentFlashcardsListEvent){
        when(event){
            is FragmentFlashcardsListEvent.FabButtonClicked -> {
                openGroupCreatorScreen(NULL_FLASHCARD_ID, _state.value.groupId!!)
            }

            is FragmentFlashcardsListEvent.ClickedClearMenuItem -> {
                selectionBehaviourHelper.clearSelectedMenuItems()
            }
            FragmentFlashcardsListEvent.ClickedDeleteMenuItem -> {
                selectionBehaviourHelper.deleteSelectedMenuItems { selectedId ->
                    deleteSelectedMenuItem(selectedId)
                }
            }
            is FragmentFlashcardsListEvent.LongClickedFlashcard -> {
                selectionBehaviourHelper.onLongClickedItem(event.id)
            }
            is FragmentFlashcardsListEvent.ShortClickedFlashcard -> {
                selectionBehaviourHelper.onShortClickedItem(event.id){
                    openGroupCreatorScreen(event.id, _state.value.groupId!!)
                }
            }
        }
    }

    private fun openGroupCreatorScreen(flashcardId: Long, groupId: Long) {
        viewModelScope.launch {
            _eventFlow.emit(
                UiEvent.OpenGroupCreatorScreen(flashcardId, groupId)
            )
        }
    }

    private fun deleteSelectedMenuItem(id: Long) {
        viewModelScope.launch {
            flashcardUseCases.deleteFlashcardById(id)
        }
    }

    private fun updateStateData(
        items: List<FlashcardListAdapterItem>
    ){
        if(items.isNotEmpty())
        {
            _state.update { previousView ->
                previousView.copy(
                    items = items,
                    recyclerViewVisibility = true,
                    emptyListNoticeVisibility = false
                )
            }
        }else{
            _state.update { previousView ->
                previousView.copy(
                    items = items,
                    recyclerViewVisibility = false,
                    emptyListNoticeVisibility = true
                )
            }
        }

        selectionBehaviourHelper.updateItemList(items)
    }


    private fun updateStateGroupId(groupId: Long){
        _state.update { previousView ->
            previousView.copy(
                groupId = groupId
            )
        }
    }

    private fun flashcardToItem(flashcard: Flashcard): FlashcardListAdapterItem {
        return FlashcardListAdapterItem(
            flashcard = flashcard,
            isSelected = isFlashcardSelected(flashcard)
        )
    }

    private fun isFlashcardSelected(flashcard: Flashcard): Boolean {
        return selectionBehaviourHelper.isIdSelected(flashcard.id!!)
    }

    private fun setToolBarVisibility(isToolBarVisible: Boolean) {
        viewModelScope.launch {
            _state.update { previousView ->
                previousView.copy(
                    isToolBarVisible = isToolBarVisible
                )
            }
        }
    }

    sealed class UiEvent{
        data class OpenGroupCreatorScreen(val flashcardId: Long, val groupId: Long): UiEvent()
    }
}
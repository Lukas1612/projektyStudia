package com.example.flashcards2.presentation.feature_flashcards_list.groups_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards2.domain.model.Col
import com.example.flashcards2.domain.model.FlashcardGroup
import com.example.flashcards2.domain.scheduler.card_state_updater.IntervalCalculatorHelper
import com.example.flashcards2.domain.use_case.col.ColUseCases
import com.example.flashcards2.domain.use_case.flashcard.FlashcardUseCases
import com.example.flashcards2.domain.use_case.groups.FlashcardGroupUseCases
import com.example.flashcards2.presentation.Constants.NULL_FLASHCARD_ID
import com.example.flashcards2.presentation.Constants.NULL_GROUP_ID
import com.example.flashcards2.presentation.feature_flashcards_list.groups_fragment.adapters.GroupAdapterItem
import com.example.flashcards2.presentation.feature_flashcards_list.helpers.SelectionBehaviourHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class FragmentFlashcardGroupListViewModel @Inject constructor(
    private val flashcardGroupUseCases: FlashcardGroupUseCases,
    private val flashcardUseCases: FlashcardUseCases,
    private val colUseCases: ColUseCases
): ViewModel() {

    private val _state = MutableStateFlow(FlashcardGroupListState())
    val state: StateFlow<FlashcardGroupListState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var getFlashcardGroupsJob: Job? = null

    private var selectionBehaviourHelper: SelectionBehaviourHelper<GroupAdapterItem>


    init {

        saveCreationTimestampSecondsIfNotYetSaved()


        selectionBehaviourHelper = object : SelectionBehaviourHelper<GroupAdapterItem>(){
            override fun updateViewData(items: List<GroupAdapterItem>) {
                updateStateData(items)
            }

            override fun doOnToolBarVisibilityChanged(isToolBarVisible: Boolean) {
                setToolBarVisibility(isToolBarVisible)
            }

            override fun createUnelectedItemFromItem(item: GroupAdapterItem): GroupAdapterItem {
                return GroupAdapterItem(
                    group = item.group,
                    isSelected = false
                )
            }

            override fun createSelectedItemFromItem(item: GroupAdapterItem): GroupAdapterItem {
                return GroupAdapterItem(
                    group = item.group,
                    isSelected = true
                )
            }

            override fun getItemId(item: GroupAdapterItem): Long {
                return item.group.id!!
            }

        }

        getFlashcardGroups()
    }

    private fun saveCreationTimestampSecondsIfNotYetSaved() {
        viewModelScope.launch {
            colUseCases.getCol().first().let { col ->
                if(col.isEmpty()){
                    val creationTimestampSeconds = IntervalCalculatorHelper().nowTimestampSecond()
                    colUseCases.saveCol(
                        Col(
                            creationTimestampSeconds = creationTimestampSeconds
                        )
                    )
                }
            }
        }
    }

    fun onEvent(event: FlashcardGroupListEvent){
        when(event)
        {
            is FlashcardGroupListEvent.ClickedMainFabButton -> {
                changeFabButtonMode()
            }

            is FlashcardGroupListEvent.ShortClickedFlashcardGroup -> {
                contractFabButton()
                selectionBehaviourHelper.onShortClickedItem(event.id){
                    goToFlashcardListFragment(event.id)
                }
            }

            is  FlashcardGroupListEvent.ClickedGroupFabButton -> {

                contractFabButton()
                openGroupCreatorScreen()
            }

            is FlashcardGroupListEvent.ClickedFlashcardFabButton  -> {

                contractFabButton()
                goToFlashcardCreatorScreen()
            }

            is FlashcardGroupListEvent.ClickedDeleteMenuItem -> {
                selectionBehaviourHelper.deleteSelectedMenuItems { selectedId ->
                    deleteSelectedMenuItem(selectedId)
                }
            }

            is FlashcardGroupListEvent.LongClickedFlashcardGroup -> {
                selectionBehaviourHelper.onLongClickedItem(event.id)
            }

            is  FlashcardGroupListEvent.ClickedClearMenuItem -> {
                selectionBehaviourHelper.clearSelectedMenuItems()
            }
        }
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

    private fun deleteSelectedMenuItem(id: Long) {
        viewModelScope.launch {
            flashcardUseCases.deleteFlashcardsByGroupId(id)
            flashcardGroupUseCases.deleteFlashcardGroupById(id)
        }
    }



   /* private fun findGroupById(groupId: Long): FlashcardGroup{
        val item = _state.value.items.find { it.group.id == groupId }
        return item!!.group
    }*/

    private fun getFlashcardGroups()
    {
        getFlashcardGroupsJob?.cancel()

        getFlashcardGroupsJob = flashcardGroupUseCases.getFlashcardGroups()
            .onEach { updatedList ->

                if(updatedList.isNotEmpty())
                {
                    Log.d("groupList", "getFlashcardGroups $updatedList" )
                    updateStateData(updatedList.map {groupToMenuItem(it)})
                }else{
                    setEmptyDataState()
                }
            }.launchIn(viewModelScope)
    }


    private fun updateStateData(items: List<GroupAdapterItem>)
    {
        _state.update { previousView ->
            previousView.copy(
                items = items,
                recyclerViewVisibility = true,
                emptyListNoticeVisibility = false
            )
        }

        selectionBehaviourHelper.updateItemList(items)
    }

    private fun setEmptyDataState(){
        _state.update { previousView ->
            previousView.copy(
                items = emptyList(),
                recyclerViewVisibility = false,
                emptyListNoticeVisibility = true
            )
        }
    }

    private fun changeFabButtonMode(){
        val expandedButtonsVisibility = !_state.value.expandedButtonsVisibility
        _state.update { previousView ->
            previousView.copy(
                expandedButtonsVisibility = expandedButtonsVisibility,
            )
        }
    }

    private fun contractFabButton(){

        if(_state.value.expandedButtonsVisibility){
            _state.update { previousView ->
                previousView.copy(
                    expandedButtonsVisibility = false,
                )
            }
        }
    }

    private fun goToFlashcardCreatorScreen(){
        viewModelScope.launch {
            _eventFlow.emit(
                UiEvent.GoToFlashcardCreatorScreen(NULL_FLASHCARD_ID, NULL_GROUP_ID)
            )
        }
    }

    private fun openGroupCreatorScreen(){
        viewModelScope.launch {
            _eventFlow.emit(
                UiEvent.OpenGroupCreatorScreen
            )
        }
    }

    private fun goToFlashcardListFragment(groupId: Long){
        viewModelScope.launch {
            _eventFlow.emit(
                UiEvent.GoToFlashcardListFragment(groupId)
            )
        }
    }

    private fun groupToMenuItem(group: FlashcardGroup): GroupAdapterItem
    {
        return GroupAdapterItem(
            group = group,
            isSelected = isGroupSelected(group)
        )
    }



    private fun isGroupSelected(group: FlashcardGroup): Boolean{
        return selectionBehaviourHelper.isIdSelected(group.id!!)
    }

    sealed class UiEvent{
        data class GoToFlashcardCreatorScreen(val flashcardId: Long, val groupId: Long) : UiEvent()
        object OpenGroupCreatorScreen: UiEvent()
        data class GoToFlashcardListFragment(val groupId: Long) : UiEvent()
    }
}
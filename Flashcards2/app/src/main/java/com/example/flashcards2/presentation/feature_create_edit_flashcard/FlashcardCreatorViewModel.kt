package com.example.flashcards2.presentation.feature_create_edit_flashcard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards2.domain.model.Flashcard
import com.example.flashcards2.domain.model.FlashcardGroup
import com.example.flashcards2.domain.scheduler.card_state_updater.CardType
import com.example.flashcards2.domain.scheduler.queue.CardQueue
import com.example.flashcards2.domain.use_case.flashcard.FlashcardUseCases
import com.example.flashcards2.domain.use_case.groups.FlashcardGroupUseCases
import com.example.flashcards2.presentation.Constants
import com.example.flashcards2.presentation.Constants.FLASHCARD_ID_KEY
import com.example.flashcards2.presentation.Constants.GROUP_ID_KEY
import com.example.flashcards2.presentation.Constants.NULL_FLASHCARD_ID
import com.example.flashcards2.presentation.Constants.NULL_GROUP_ID
import com.example.flashcards2.presentation.feature_create_edit_flashcard.adapters.GroupsAdapterListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
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
class FlashcardCreatorViewModel @Inject constructor(
    private val flashcardGroupUseCases: FlashcardGroupUseCases,
    private val flashcardUseCases: FlashcardUseCases,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _flashcardGroupState = MutableStateFlow(FlashcardGroupState())
    val flashcardGroupState : StateFlow<FlashcardGroupState> = _flashcardGroupState

    private val _flashcardWordState = MutableStateFlow(FlashcardTextFieldState(
        hint = "write a word e.g. sugar glider"
    ))
    val flashcardWordState: StateFlow<FlashcardTextFieldState> = _flashcardWordState

    private val _flashcardTranslationState = MutableStateFlow(FlashcardTextFieldState(
        hint = "write a translation e.g. lotopałanka"
    ))
    val flashcardTranslationState: StateFlow<FlashcardTextFieldState> = _flashcardTranslationState

    private val _buttonsState = MutableStateFlow(FlashcardCreatorButtonsState())
    val buttonsState: StateFlow<FlashcardCreatorButtonsState> = _buttonsState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var flashcardGroups: List<FlashcardGroup> = emptyList()
    private var editedFlashcard: Flashcard? = null

    private var getFlashcardGroupsJob: Job? = null

    private var isSavedStateHandleAlreadyLoaded: Boolean = false


    init{
        init()
    }

    private fun init()
    {
        createPlaceholderFlashcardGroup()
        initUpdatingFlashcardGroups()
    }

    private fun createPlaceholderFlashcardGroup()
    {
        val placeholderFlashcardGroup = FlashcardGroup(
            id = 1,
            name = Constants.AUTOCOMPLETE_FLASHCARD_GROUP_NAME,
            date = 0L
        )
        _flashcardGroupState.update { previousView ->
            previousView.copy(
                selectedGroup = placeholderFlashcardGroup
            )
        }
    }


    private fun initUpdatingFlashcardGroups(){

        getFlashcardGroupsJob?.cancel()

        getFlashcardGroupsJob = flashcardGroupUseCases.getFlashcardGroups()
            .onEach { updatedList ->

                flashcardGroups = updatedList
                createItemList()

                if(isSavedStateHandleAlreadyLoaded)
                {
                    selectTheLatestFlashcardGroup()

                }else if(savedStateHandleDataIsNotEmpty())
                {
                    retrieveSavedStateHandleData()
                    isSavedStateHandleAlreadyLoaded = true
                }else
                {
                    selectTheLatestFlashcardGroup()
                    isSavedStateHandleAlreadyLoaded = true
                }

            }.launchIn(viewModelScope)
    }

    private fun createItemList() {
        val items: MutableList<GroupsAdapterListItem> = mutableListOf()

        items.add(GroupsAdapterListItem.GroupCreatorItem)

        if(flashcardGroups.isNotEmpty()){
            flashcardGroups.forEach { group ->
                items.add(GroupsAdapterListItem.FlashcardGroupItem(group))
            }
        }

        _flashcardGroupState.update { previousView ->
            previousView.copy(
                items = items
            )
        }
    }


    private fun selectTheLatestFlashcardGroup(){

        if(flashcardGroups.isNotEmpty())
        {
            val selectedGroup = flashcardGroups[0]

            _flashcardGroupState.update { previousView ->
                previousView.copy(
                    selectedGroup = selectedGroup
                )
            }
        }
    }

    private fun savedStateHandleDataIsNotEmpty(): Boolean{
        return savedStateHandle.get<Long>(FLASHCARD_ID_KEY) != NULL_FLASHCARD_ID ||  savedStateHandle.get<Long>(
            GROUP_ID_KEY) != NULL_GROUP_ID
    }



    private fun retrieveSavedStateHandleData() {
        savedStateHandle.get<Long>(FLASHCARD_ID_KEY)?.let { id ->
            if(id != NULL_FLASHCARD_ID){
                loadTheFlashcard(id)
                setDeleteButtonVisible()
            }
        }

        savedStateHandle.get<Long>(GROUP_ID_KEY)?.let { groupId ->
            if(groupId != NULL_GROUP_ID){
                loadTheSelectedGroup(groupId)
            }
        }
    }

    private fun loadTheFlashcard(id: Long){
        viewModelScope.launch {

            val flashcardTmp = async { flashcardUseCases.getFlashcardById(id)}

            editedFlashcard = flashcardTmp.await()

            _flashcardWordState.update { previousState ->
                previousState.copy(
                    text = flashcardTmp.await()!!.front,
                )
            }

            _flashcardTranslationState.update { previousState ->
                previousState.copy(
                    text =  flashcardTmp.await()!!.back,
                )
            }
        }
    }

    private fun loadTheSelectedGroup(groupId: Long) {
        viewModelScope.launch {
            val selectedGroup = async {flashcardGroupUseCases.getFlashcardGroupById(groupId) }

            _flashcardGroupState.update { previousView ->
                previousView.copy(
                    selectedGroup = selectedGroup.await()
                )
            }
        }
    }

    private fun setDeleteButtonVisible(){
        _buttonsState.update { previousView ->
            previousView.copy(
                isDeleteButtonVisible = true
            )
        }
    }

     fun onEvent(event: FlashcardCreatorEvent){
        when (event){
            is FlashcardCreatorEvent.SelectedFlashcardGroup -> {

                val selectedFlashcardGroup = flashcardGroups.find { it.name == event.value }

                _flashcardGroupState.update { previousView ->
                    previousView.copy(
                        selectedGroup = selectedFlashcardGroup
                    )
                }

            }

            is FlashcardCreatorEvent.EnteredWord -> {

                _flashcardWordState.update { previousView ->
                    previousView.copy(
                        text = event.value
                    )
                }
            }

            is FlashcardCreatorEvent.EnteredTranslation -> {
                _flashcardTranslationState.update { previousView ->
                    previousView.copy(
                        text = event.value
                    )
                }
            }

            is FlashcardCreatorEvent.SaveFlashcardGroup -> {
                val newGroup = FlashcardGroup(
                    name = event.value,
                    date = System.currentTimeMillis()
                )

                viewModelScope.launch {
                    flashcardGroupUseCases.addFlashcardGroup(newGroup)
                }
            }

            is FlashcardCreatorEvent.SelectedExpandAllGroupsList -> {

                viewModelScope.launch {
                    _eventFlow.emit(
                        UiEvent.ShowAllGroupsPopUp
                    )
                }
            }

            is FlashcardCreatorEvent.SaveFlashcard -> {
                if(areAllFieldsFilled())
                {
                    updateChosenFlashcardGroupDate()
                    saveFlashcard()
                    clearEditTextFields()

                }else
                {
                    showFillAllFieldsAlert()
                }

            }

            is FlashcardCreatorEvent.OpenFlashcardGroupCreator -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        UiEvent.ShowCreateGroupPopUp
                    )
                }
            }

            is FlashcardCreatorEvent.DeleteFlashcard -> {
                deleteEditedFlashcard()
                finishActivity()
            }
        }
    }

    private fun deleteEditedFlashcard() {
        viewModelScope.launch {
            flashcardUseCases.deleteFlashcard(editedFlashcard!!)
        }
    }

    private fun finishActivity() {
        viewModelScope.launch {
            _eventFlow.emit(
                UiEvent.FinishActivity
            )
        }
    }

    private fun showFillAllFieldsAlert() {
        viewModelScope.launch {
            _eventFlow.emit(
                UiEvent.ShowFillAllFieldsToast
            )
        }
    }

    private fun updateChosenFlashcardGroupDate()
    {
        val chosenFlashcardGroup = _flashcardGroupState.value.selectedGroup
        chosenFlashcardGroup!!.date = System.currentTimeMillis()

        viewModelScope.launch {
            flashcardGroupUseCases.addFlashcardGroup(chosenFlashcardGroup)
        }
    }


    private fun saveFlashcard() {

        viewModelScope.launch {
            val flashcardTmp = Flashcard(
                group_id = _flashcardGroupState.value.selectedGroup!!.id,
                front = _flashcardWordState.value.text,
                back = _flashcardTranslationState.value.text,
                ctype = CardType.New,
                queue = CardQueue.New,
                due = 0,
                interval = 0,
                ease_factor = 0,
                reps = 0,
                lapses = 0,
                remaining_steps = 0,
                original_due = 0,
                original_deck_id = 0,
                original_position = null,
                desired_retention = null,
                custom_data = ""
            )

            flashcardUseCases.addFlashcard(flashcardTmp)
        }
    }

    fun clearEditTextFields(){
        viewModelScope.launch {
            _flashcardTranslationState.update { previousState ->
                previousState.copy(
                    text = ""
                )
            }

            _flashcardWordState.update { previousState ->
                previousState.copy(
                    text = ""
                )
            }
        }
    }



    private fun areAllFieldsFilled(): Boolean {
        return (
                _flashcardWordState.value.text.isNotEmpty()
                        && _flashcardTranslationState.value.text.isNotEmpty()
                )
    }

    sealed class UiEvent{
        object ShowCreateGroupPopUp : UiEvent()
        object ShowAllGroupsPopUp : UiEvent()
        object ShowFillAllFieldsToast : UiEvent()
        object FinishActivity : UiEvent()
    }

}
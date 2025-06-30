package com.example.flashcards2.presentation.feature_create_edit_group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards2.domain.model.FlashcardGroup
import com.example.flashcards2.domain.use_case.groups.FlashcardGroupUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEditGroupViewModel @Inject constructor(
    private val flashcardGroupUseCases: FlashcardGroupUseCases
): ViewModel()  {

    private var groupName: String = ""

    private val hint = "write group name"
    fun getHint() = hint

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun onEvent(event: CreateEditGroupEvent)
    {
        when(event){
            is CreateEditGroupEvent.SaveGroup -> {
                saveGroup(event.groupName)
            }
        }

    }

    private fun saveGroup(groupName: String) {

        if(groupName.isNotEmpty())
        {
            addNewGroup(groupName)
            finish()

        }else{
            showFillAllFieldsAlert()
        }

    }

    private fun addNewGroup(groupName: String){
        val savedFlashcardGroup = FlashcardGroup(
            name = groupName,
            date = System.currentTimeMillis()
        )

        viewModelScope.launch {
            flashcardGroupUseCases.addFlashcardGroup(savedFlashcardGroup)
        }
    }


    private fun finish() {
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


    sealed class UiEvent{
        object FinishActivity : UiEvent()
        object ShowFillAllFieldsToast : UiEvent()
    }
}
package com.example.locationdescriptor.location_descriptor.presentation.add_edit_photo


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locationdescriptor.location_descriptor.domain.model.PhotoDescription
import com.example.locationdescriptor.location_descriptor.domain.use_case.description.PhotoDescriptionUseCases
import com.example.locationdescriptor.location_descriptor.domain.use_case.photo.PhotoUseCases
import com.example.locationdescriptor.location_descriptor.presentation.Constants.NULL_PHOTO_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AddEditDescriptionViewModel @Inject constructor(
    private val descriptionUseCases: PhotoDescriptionUseCases,
    private val photoUseCases: PhotoUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {


    private val _state = MutableStateFlow(AddEditPhotoState())
    val state: StateFlow<AddEditPhotoState>  = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var folderName: String = "root"




    fun onEvent(event: AddEditDescriptionEvent)
    {
        when(event)
        {
            is AddEditDescriptionEvent.EnteredLatitude -> {

                _state.update { previousState ->
                    previousState.copy(
                        latitude = event.value
                    )
                }
            }

            is AddEditDescriptionEvent.EnteredLongitude -> {

                _state.update { previousState ->
                    previousState.copy(
                        longitude = event.value
                    )
                }
            }

            is AddEditDescriptionEvent.EnteredNote -> {
                if(event.value != null)
                {
                    _state.update { previousState ->
                        previousState.copy(
                            note = event.value
                        )
                    }
                }

            }

            is AddEditDescriptionEvent.EnteredTitle ->
            {
                _state.update { previousState ->
                    previousState.copy(
                        title = event.value
                    )
                }
            }

            is AddEditDescriptionEvent.TakenPhoto ->
            {
                _state.update { previousState ->
                    previousState.copy(
                        photo = event.value
                    )
                }
            }


            is AddEditDescriptionEvent.CancelButtonClicked ->
            {
                viewModelScope.launch{
                    _eventFlow.emit(
                        UiEvent.AskIfUserConfirmToCancelWithoutSaving
                    )
                }
            }

            is AddEditDescriptionEvent.SaveButtonClicked ->
            {
                viewModelScope.launch{
                    _eventFlow.emit(
                        UiEvent.AskIfUserConfirmSavingThePhoto
                    )
                }
            }

            is AddEditDescriptionEvent.CancelConfirmed ->
            {
                viewModelScope.launch{
                    _eventFlow.emit(
                        UiEvent.FinishActivity
                    )
                }
            }

            is AddEditDescriptionEvent.SaveConfirmed ->
            {
                savePhoto()
            }


            is AddEditDescriptionEvent.GetCurrentLocation ->
            {
                viewModelScope.launch {
                    _eventFlow.emit(
                        UiEvent.GetCurrentLocation
                    )
                }
            }
        }
    }


    private fun savePhoto() {
        viewModelScope.launch(Dispatchers.IO) {

            val description = PhotoDescription(
                title = _state.value.title!!,
                note = _state.value.note,
                latitude=  _state.value.latitude!!,
                longitude = _state.value.longitude!!,
                folderName = folderName,
                id =  _state.value.id
            )

            try {
                if (photoUseCases.savePhoto(
                        _state.value.photo!!,
                        _state.value.title!!
                    )) {
                    descriptionUseCases.addPhotoDescription(description)
                }

            } catch (e: IOException) {
                println("ERROR: $e")
            }

            _eventFlow.emit(
                UiEvent.FinishActivity
            )
        }
    }


    init{

        savedStateHandle.get<Int>("id")?.let { id ->
            if(id == NULL_PHOTO_ID)
            {
                viewModelScope.launch {
                    _eventFlow.emit(
                        UiEvent.GetCurrentLocation
                    )
                }

                viewModelScope.launch {
                    _eventFlow.emit(
                        UiEvent.TakePhoto
                    )
                }
            }else
            {
                viewModelScope.launch {
                    descriptionUseCases.getPhotoDescription(id)?.also { description ->

                        _state.update { previousState ->
                            previousState.copy(
                                title = description.title,
                                note = description.note,
                                latitude = description.latitude,
                                longitude = description.longitude,
                                id = id
                            )
                        }
                        photoUseCases.getPhoto(description.title).also {

                            _state.update { previousState ->
                                previousState.copy(
                                    photo = it
                                )
                            }
                        }

                    }
                }
            }
        }


        savedStateHandle.get<String>("folder_name")?.let { name ->

            if(name.isNotBlank())
            {
                folderName = name
            }
        }

    }


    sealed class UiEvent{
        object TakePhoto: UiEvent()
        object GetCurrentLocation: UiEvent()
        object FinishActivity: UiEvent()
        object AskIfUserConfirmSavingThePhoto: UiEvent()
        object AskIfUserConfirmToCancelWithoutSaving: UiEvent()
    }


}
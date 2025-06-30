package com.example.locationdescriptor.location_descriptor.presentation.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.locationdescriptor.location_descriptor.domain.model.FolderDescriptor
import com.example.locationdescriptor.location_descriptor.domain.model.PhotoDescription
import com.example.locationdescriptor.location_descriptor.domain.use_case.description.PhotoDescriptionUseCases
import com.example.locationdescriptor.location_descriptor.domain.use_case.folder.FolderDescriptorUseCases
import com.example.locationdescriptor.location_descriptor.domain.use_case.photo.PhotoUseCases
import com.example.locationdescriptor.location_descriptor.presentation.Constants.NULL_PHOTO_ID
import com.example.locationdescriptor.location_descriptor.presentation.WorkerKeys
import com.example.locationdescriptor.location_descriptor.presentation.photos.adapters.ListItem
import com.example.locationdescriptor.location_descriptor.presentation.photos.folders_tree.Folder
import com.example.locationdescriptor.location_descriptor.presentation.photos.pdf_create.CreatePdfWorker
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val photoDescriptionUseCases: PhotoDescriptionUseCases,
    private val folderDescriptorUseCases: FolderDescriptorUseCases,
    private val photoUseCases: PhotoUseCases,
    private val workManager: WorkManager
): ViewModel() {

    private val _state = MutableStateFlow(PhotosState())
    val state: StateFlow<PhotosState> = _state
    val itemList: ItemList = ItemList()


    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var isFabButtonExpanded = false


    private var getPhotosJob: Job? = null
    private var getFoldersJob: Job? = null



    fun getPhotoDirectoryPath(): String?{
        return photoUseCases.getPhotoPath()
    }

    fun onEvent(event: PhotosEvent) {

        when (event) {
            is PhotosEvent.OnPhotoClicked -> {

                collapseFabButton()

                val name = itemList.getCurFolder().name
                val selectedPhotoId = event.value.id

                viewModelScope.launch {
                    _eventFlow.emit(
                        UiEvent.OpenPhotoScreen(
                            id = selectedPhotoId!!,
                            folderName = name
                        )
                    )
                }

            }

            is PhotosEvent.MainFabButtonCLicked -> {

                if (isFabButtonExpanded) {
                    collapseFabButton()
                } else {
                    expandFabButton()
                }
            }

            is PhotosEvent.PhotoFabButtonCLicked -> {
                collapseFabButton()

                viewModelScope.launch {
                    _eventFlow.emit(
                        UiEvent.OpenPhotoScreen(
                            id = NULL_PHOTO_ID, //you are taking a new photo so it doesn't have an id yet
                            folderName = itemList.getCurFolder().name
                        )
                    )
                }
            }

            is PhotosEvent.FolderFabButtonCLicked -> {

                collapseFabButton()

                viewModelScope.launch {
                    _eventFlow.emit(
                        UiEvent.OpenFolderCreationPopUp
                    )
                }
            }

            is PhotosEvent.OnFolderClicked -> {
                goToTheClickedFolder(event.name)
            }

            is PhotosEvent.OnParentFolderClicked -> {
                goToParentFolder()
            }

            is PhotosEvent.OnNewFolderCreated -> {
                createNewFolder(event.value)
            }

            is PhotosEvent.OnBackPressed -> {
                if (itemList.getCurFolder().name != "root") {
                    goToParentFolder()
                } else {
                    viewModelScope.launch {
                        _eventFlow.emit(
                            UiEvent.FinishApp
                        )
                    }
                }
            }

            is PhotosEvent.OnDelete -> {

                when (event.value) {
                    is ListItem.FolderItem -> {
                        val deletedFolderName = event.value.name
                        val deletedFolder = itemList.searchFolderByName(deletedFolderName)
                        deleteFolderRecursive(deletedFolder!!)
                    }

                    is ListItem.PhotoItem -> {
                        val deletedPhotoDescription = event.value.photoDescription
                        deletePhoto(deletedPhotoDescription)
                    }

                    else -> {}
                }
            }

            is PhotosEvent.OnItemsSelectionCleared -> {
                unselectAll()
            }

            //The data in the event has to be the whole item instead of its position
            //on the list to avoid bugs after removing one of the elements
            //from the list.
            is PhotosEvent.OnSelect -> {
                val selectedItem = event.value

                selectItem(selectedItem)

            }

            is PhotosEvent.OnMove -> {

                moveSelectedPhotos()
                moveSelectedFolders()
                unselectAll()
            }

            is PhotosEvent.OnMultipleDeleteClicked -> {
                if (!itemList.thereIsNoSelectedItem()) {
                    viewModelScope.launch {
                        _eventFlow.emit(
                            UiEvent.ShowMultiDeleteAlertWindow
                        )
                    }
                }
            }

            is PhotosEvent.OnMultipleDeleteConfirmedByUser -> {
                deleteSelectedPhotos()
                deleteSelectedFolders()
                unselectAll()
            }

            is PhotosEvent.OnCreatePdf -> {
                if(fileExists(event.value))
                {
                    viewModelScope.launch{
                        _eventFlow.emit(
                            UiEvent.FileNameExistWindow
                        )
                    }
                }else if(itemList.thereIsNoSelectedItem()) {
                    viewModelScope.launch {
                        _eventFlow.emit(
                            UiEvent.OpenNonDataSelectedPopUp
                        )
                    }
                }
                else if(event.value.isNotBlank())
                {
                    createPdf(event.value)
                    unselectAll()
                }
            }
        }
    }


    //**************** createNewFolder ************************
    //check if folder name exists
    private fun CoroutineScope.folderNameExists(folderName: String, callback: (Boolean) -> Unit)
    {
        launch(IO) {
            if (folderName.isNotBlank()) {
                callback(folderDescriptorUseCases.checkIfFolderNameIsAlreadyUsed(folderName) || folderName == "root")
            }
        }
    }

    //create a new folder if there isn't a folder with a given name
    private fun createNewFolder(folderName: String) {
        viewModelScope.folderNameExists(folderName){ folderNameExists ->
            if(!folderNameExists)
            {
                viewModelScope.launch(IO) {
                    folderDescriptorUseCases.addFolderDescriptor(
                        FolderDescriptor(
                            name = folderName,
                            parentName = itemList.getCurFolder().name,
                            depth = itemList.getCurFolder().depth + 1,
                            id = null
                        )
                    )
                }
            }else
            {
                viewModelScope.launch(IO) {
                    _eventFlow.emit(
                        UiEvent.FolderNameExistWindow
                    )
                }
            }

        }
    }
    //****************************************

    private fun goToTheClickedFolder(folderName: String) {

        itemList.goToTheClickedFolder(folderName)

        if(itemList.youAreInsideSelectedFolder())
        {
            viewModelScope.launch {
                _eventFlow.emit(
                    UiEvent.ChangeMultiDeleteButtonVisibility(false)
                )
            }

            viewModelScope.launch {
                _eventFlow.emit(
                    UiEvent.ChangeMoveFoldersButtonVisibility(false)
                )
            }
        }

        notifyTheDataHasChanged()

    }




    private fun selectItem(selectedItem: ListItem)
    {
        when(selectedItem)
        {
            is ListItem.PhotoItem -> {
                itemList.selectPhotoItem(selectedItem)
            }

            is ListItem.FolderItem -> {
                itemList.selectFolderItem(selectedItem)
            }

            else -> {}
        }

        if(itemList.theFirstItemHasBeenSelected())
        {
            viewModelScope.launch {
                _eventFlow.emit(
                    UiEvent.ChangeSelectionClearButtonVisibility(true)
                )
            }
        }

        if(itemList.thereIsNoSelectedItem())
        {
            viewModelScope.launch {
                _eventFlow.emit(
                    UiEvent.ChangeSelectionClearButtonVisibility(false)
                )
            }
        }

        notifyTheDataHasChanged()
    }


    private fun moveSelectedFolders() {


        val movedFolderDescriptors = itemList.makeAListOfMovedFolderDescriptors()

        movedFolderDescriptors.forEach { movedFolderDescriptor ->

            viewModelScope.launch {
                folderDescriptorUseCases.addFolderDescriptor(movedFolderDescriptor)
            }
        }
    }

    private fun moveSelectedPhotos() {

        val movedPhotoDescriptors = itemList.makeAListOfMovedPhotoDescriptors()

        movedPhotoDescriptors?.forEach { description ->
            viewModelScope.launch {
                photoDescriptionUseCases.addPhotoDescription(description)
            }
        }

    }

    private fun deleteSelectedPhotos()
    {
        if (itemList.getSelectedPhotoDescriptions().isNotEmpty()) {
            itemList.getSelectedPhotoDescriptions().forEach { photo ->
                deletePhoto(photo)
            }
        }
    }

    private fun deleteSelectedFolders()
    {
        if (itemList.getSelectedFoldersNames().isNotEmpty()) {
            val selectedFolders = itemList.getSelectedFoldersNames().map { name -> itemList.searchFolderByName(name)}
            selectedFolders.forEach { folder ->
                deleteFolderRecursive(folder!!)
            }
        }
    }

    private fun fileExists(fileName: String): Boolean{
        return photoUseCases.doesFileExist(fileName)
    }


    private fun unselectAll() {

        itemList.unselectAll()

        viewModelScope.launch {
            _eventFlow.emit(
                UiEvent.ChangeSelectionClearButtonVisibility(false)
            )
        }

        viewModelScope.launch {
            _eventFlow.emit(
                UiEvent.ChangeMultiDeleteButtonVisibility(true)
            )
        }

        viewModelScope.launch {
            _eventFlow.emit(
                UiEvent.ChangeMoveFoldersButtonVisibility(true)
            )
        }

        notifyTheDataHasChanged()
    }


    init {
        getDescriptions()
        getFolderDescriptors()
    }


    private fun getFolderDescriptors()
    {
        getFoldersJob?.cancel()
        getFoldersJob = folderDescriptorUseCases.getFolderDescriptors()
            .onEach { updatedList ->


                updatedList.forEach {
                    println(" name: " + it.name + " | depth: " + it.depth + " | parentName: " + it.parentName + " | id: " + it.id)
                }
                itemList.setFolderDescriptors(updatedList)
                itemList.notifyDataSetChanged()
                notifyTheDataHasChanged()

            }.launchIn(viewModelScope)
    }

    private fun getDescriptions()
    {
        getPhotosJob?.cancel()
        getPhotosJob = photoDescriptionUseCases.getPhotoDescriptions()
            .onEach { updatedList ->

                itemList.setPhotoDescriptions(updatedList)
                itemList.notifyDataSetChanged()
                notifyTheDataHasChanged()

            }.launchIn(viewModelScope)

    }

    private fun notifyTheDataHasChanged()
    {
        val items = itemList.getItemList()


        if(items!!.size != _state.value.items.size)
        {
            _state.update { previousView ->
                previousView.copy(
                    items = items
                )
            }

            viewModelScope.launch {
                _eventFlow.emit(
                    UiEvent.UpdateRecyclerView
                )
            }
        }else
        {
            _state.update { previousView ->
                previousView.copy(
                    items = items
                )
            }
        }
    }

    private fun deleteFolderRecursive(folder: Folder){
        folder.forEachDepthFirst { childFolder ->
            viewModelScope.launch {
                childFolder.photoDescriptions.forEach { description ->

                    if(photoUseCases.deletePhoto(description.title))
                    {
                        photoDescriptionUseCases.deletePhotoDescription(description)
                    }
                }

                folderDescriptorUseCases.deleteFolderDescriptorByName(childFolder.name)
            }

        }
    }

    private fun deletePhoto(photo: PhotoDescription) {

        viewModelScope.launch {
            if(photoUseCases.deletePhoto(photo.title))
            {
                photoDescriptionUseCases.deletePhotoDescription(photo)
            }
        }
    }


    private fun goToParentFolder()
    {
        itemList.goToParentFolder()

        if(!itemList.youAreInsideSelectedFolder())
        {
            viewModelScope.launch {
                _eventFlow.emit(
                    UiEvent.ChangeMultiDeleteButtonVisibility(true)
                )
            }

            viewModelScope.launch {
                _eventFlow.emit(
                    UiEvent.ChangeMoveFoldersButtonVisibility(true)
                )
            }
        }

        notifyTheDataHasChanged()
    }

    private fun collapseFabButton()
    {
        isFabButtonExpanded = false

        viewModelScope.launch {
            _eventFlow.emit(
                UiEvent.CollapseFab
            )
        }
    }


    private fun expandFabButton()
    {
        isFabButtonExpanded = true

        viewModelScope.launch {
            _eventFlow.emit(
                UiEvent.ExpandFab
            )
        }
    }


    private fun createPdf(fileName: String)
    {

        val selectedPhotosListJson = Gson().toJson(itemList.selectedPhotosList())

        val data = workDataOf(
            WorkerKeys.KEY_MY_JSONARRAY to selectedPhotosListJson,
            WorkerKeys.KEY_MY_FILE_NAME to fileName
        )

        startPdfCreatingWorkRequest(data)

    }

    private fun startPdfCreatingWorkRequest(data: Data)
    {
        val workRequest = OneTimeWorkRequestBuilder<CreatePdfWorker>()
            .setInputData(data)
            .build()

        workManager.enqueue(workRequest)
    }


    sealed class UiEvent{
        data class OpenPhotoScreen(val id: Int, val folderName: String): UiEvent()
        object ShowMultiDeleteAlertWindow : UiEvent()
        object FolderNameExistWindow : UiEvent()
        object FileNameExistWindow : UiEvent()
        object OpenFolderCreationPopUp : UiEvent()
        object OpenNonDataSelectedPopUp : UiEvent()
        object FinishApp : UiEvent()
        object ExpandFab : UiEvent()
        object CollapseFab : UiEvent()
        object UpdateRecyclerView : UiEvent()
        data class ChangeMultiDeleteButtonVisibility(val value: Boolean) : UiEvent()
        data class ChangeSelectionClearButtonVisibility(val value: Boolean) : UiEvent()
        data class ChangeMoveFoldersButtonVisibility(val value: Boolean) : UiEvent()
    }

}
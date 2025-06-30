package com.example.locationdescriptor.location_descriptor.presentation.photos


import com.example.locationdescriptor.location_descriptor.domain.model.PhotoDescription
import com.example.locationdescriptor.location_descriptor.presentation.photos.adapters.ListItem

sealed class PhotosEvent{
    data class OnPhotoClicked(val value: PhotoDescription): PhotosEvent()
    data class OnFolderClicked(val name: String): PhotosEvent()
    object OnParentFolderClicked: PhotosEvent()
    data class OnNewFolderCreated(val value: String): PhotosEvent()
    data class OnDelete(val value: ListItem): PhotosEvent()
    data class OnSelect(val value: ListItem): PhotosEvent()
    object OnMove: PhotosEvent()
    object OnMultipleDeleteClicked: PhotosEvent()
    object OnMultipleDeleteConfirmedByUser: PhotosEvent()
    object MainFabButtonCLicked: PhotosEvent()
    object PhotoFabButtonCLicked: PhotosEvent()
    object FolderFabButtonCLicked: PhotosEvent()
    object OnBackPressed: PhotosEvent()
    object OnItemsSelectionCleared: PhotosEvent()
    data class OnCreatePdf(val value: String): PhotosEvent()
}

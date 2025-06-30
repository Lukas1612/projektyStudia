package com.example.locationdescriptor.location_descriptor.presentation.photos.adapters

import com.example.locationdescriptor.location_descriptor.domain.model.PhotoDescription

sealed class ListItem{
    object ParentFolderItem: ListItem()
    data class FolderItem(val name: String, var isSelected: Boolean): ListItem()
    data class PhotoItem(val photoDescription: PhotoDescription, var isSelected: Boolean): ListItem()
}

package com.example.locationdescriptor.location_descriptor.presentation.photos

import com.example.locationdescriptor.location_descriptor.domain.model.FolderDescriptor
import com.example.locationdescriptor.location_descriptor.domain.model.PhotoDescription
import com.example.locationdescriptor.location_descriptor.presentation.photos.adapters.ListItem
import com.example.locationdescriptor.location_descriptor.presentation.photos.folders_tree.Folder

data class PhotosState(
    val items: List<ListItem> = emptyList(),
)

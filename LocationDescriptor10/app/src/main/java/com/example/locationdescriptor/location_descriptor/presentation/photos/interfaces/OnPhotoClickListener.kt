package com.example.locationdescriptor.location_descriptor.presentation.photos.interfaces

import com.example.locationdescriptor.location_descriptor.domain.model.PhotoDescription

interface OnPhotoClickListener {
    fun onClick(photoDescription: PhotoDescription)
}
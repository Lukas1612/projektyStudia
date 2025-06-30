package com.example.locationdescriptor.location_descriptor.presentation.photos.adapters

import android.widget.ImageView

interface PhotosRVAdapterImageLoader {
    fun loadImageToThumbnail(thumbnail: ImageView, photoName: String)
}
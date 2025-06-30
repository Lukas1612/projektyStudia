package com.example.locationdescriptor.location_descriptor.presentation.photos.pdf_create

import android.graphics.Bitmap

data class PageData(
    val note: String,
    val coordinates: String,
    val photo: Bitmap
)

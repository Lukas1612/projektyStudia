package com.example.locationdescriptor.location_descriptor.presentation.add_edit_photo

import android.graphics.Bitmap

data class AddEditPhotoState(
    val title: String? = "",
    val note: String? = "",
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0,
    val id: Int? = null,
    val photo: Bitmap? = null
)

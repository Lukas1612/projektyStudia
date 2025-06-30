package com.example.locationdescriptor.location_descriptor.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
data class PhotoDescription(
    val title: String,
    val note: String?,
    val latitude: Double,
    val longitude: Double,
    val folderName: String,
    val id: Int?
)

class InvalidPhotoException(message: String): Exception(message)

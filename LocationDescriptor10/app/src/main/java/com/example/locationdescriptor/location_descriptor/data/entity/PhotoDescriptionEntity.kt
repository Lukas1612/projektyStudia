package com.example.locationdescriptor.location_descriptor.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.locationdescriptor.location_descriptor.domain.model.PhotoDescription

@Entity(
    indices = [Index(value = ["title"], unique = true)]
)
data class PhotoDescriptionEntity(
    val title: String,
    val note: String?,
    val latitude: Double,
    val longitude: Double,
    val folderName: String,
    @PrimaryKey val id: Int? = null
){
    fun toPhotoDescription():PhotoDescription
    {
        return PhotoDescription(
            title = title,
            note = note,
            latitude = latitude,
            longitude = longitude,
            folderName = folderName,
            id = id
        )
    }
}
class InvalidPhotoException(message: String): Exception(message)
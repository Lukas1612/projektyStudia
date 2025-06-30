package com.example.locationdescriptor.location_descriptor.domain.repository

import com.example.locationdescriptor.location_descriptor.domain.model.PhotoDescription
import kotlinx.coroutines.flow.Flow

interface DescriptionRepository {
    fun getPhotos(): Flow<List<PhotoDescription>>
    suspend fun getPhotoById(id: Int): PhotoDescription?
    suspend fun insertPhoto(photo: PhotoDescription)
    suspend fun deletePhoto(photo: PhotoDescription)
}
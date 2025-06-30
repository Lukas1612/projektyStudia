package com.example.locationdescriptor.location_descriptor.domain.repository

import com.example.locationdescriptor.location_descriptor.domain.model.FolderDescriptor
import kotlinx.coroutines.flow.Flow

interface FolderDescriptorRepository {

    fun getFolderDescriptors(): Flow<List<FolderDescriptor>>

    suspend fun insertFolderDescriptor(folderDescriptor: FolderDescriptor)
    suspend fun deleteFolderDescriptor(folderDescriptor: FolderDescriptor)
    suspend fun deleteFolderDescriptorByName(name: String)
    suspend fun nameExist(name: String): Boolean
}
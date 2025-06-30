package com.example.locationdescriptor.location_descriptor.domain.use_case.folder

import com.example.locationdescriptor.location_descriptor.domain.model.FolderDescriptor
import com.example.locationdescriptor.location_descriptor.domain.repository.FolderDescriptorRepository
import kotlinx.coroutines.flow.Flow

class GetFolderDescriptors(
    private val repository: FolderDescriptorRepository
) {
    operator fun invoke(): Flow<List<FolderDescriptor>>
    {
        return repository.getFolderDescriptors()
    }
}
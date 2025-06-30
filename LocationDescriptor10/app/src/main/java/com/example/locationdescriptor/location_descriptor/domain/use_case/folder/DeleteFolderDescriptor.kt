package com.example.locationdescriptor.location_descriptor.domain.use_case.folder

import com.example.locationdescriptor.location_descriptor.domain.model.FolderDescriptor
import com.example.locationdescriptor.location_descriptor.domain.repository.FolderDescriptorRepository

class DeleteFolderDescriptor(
    private val repository: FolderDescriptorRepository
) {

    suspend operator fun invoke(folderDescriptor: FolderDescriptor)
    {
        repository.deleteFolderDescriptor(folderDescriptor)
    }
}
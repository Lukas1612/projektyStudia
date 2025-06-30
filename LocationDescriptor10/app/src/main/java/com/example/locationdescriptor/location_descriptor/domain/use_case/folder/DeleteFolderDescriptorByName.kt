package com.example.locationdescriptor.location_descriptor.domain.use_case.folder

import com.example.locationdescriptor.location_descriptor.domain.repository.FolderDescriptorRepository

class DeleteFolderDescriptorByName(
    private val repository: FolderDescriptorRepository
) {

    suspend operator fun invoke(name: String)
    {
        repository.deleteFolderDescriptorByName(name)
    }
}
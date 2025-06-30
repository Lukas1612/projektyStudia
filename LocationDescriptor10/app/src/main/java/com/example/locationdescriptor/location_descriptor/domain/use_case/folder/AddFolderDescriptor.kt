package com.example.locationdescriptor.location_descriptor.domain.use_case.folder

import android.database.sqlite.SQLiteConstraintException
import com.example.locationdescriptor.location_descriptor.data.entity.InvalidFolderException
import com.example.locationdescriptor.location_descriptor.domain.model.FolderDescriptor
import com.example.locationdescriptor.location_descriptor.domain.repository.FolderDescriptorRepository

class AddFolderDescriptor(
    private val repository: FolderDescriptorRepository
) {
    @Throws(InvalidFolderException::class)
    suspend operator fun invoke(folderDescriptor: FolderDescriptor){

        if(folderDescriptor.name.isBlank())
        {
            throw InvalidFolderException("folder name can't be empty")
        }

        if(folderDescriptor.parentName.isBlank())
        {
            throw InvalidFolderException("folder parent name can't be empty")
        }

        repository.insertFolderDescriptor(folderDescriptor)
    }
}
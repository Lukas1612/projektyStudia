package com.example.locationdescriptor.location_descriptor.domain.use_case.folder

data class FolderDescriptorUseCases(
    val addFolderDescriptor: AddFolderDescriptor,
    val deleteFolderDescriptor: DeleteFolderDescriptor,
    val getFolderDescriptors: GetFolderDescriptors,
    val checkIfFolderNameIsAlreadyUsed: CheckIfFolderNameIsAlreadyUsed,
    val deleteFolderDescriptorByName: DeleteFolderDescriptorByName
)

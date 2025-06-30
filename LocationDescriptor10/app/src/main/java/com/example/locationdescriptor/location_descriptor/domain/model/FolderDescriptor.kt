package com.example.locationdescriptor.location_descriptor.domain.model

data class FolderDescriptor(
    val name: String,
    val parentName: String,
    val depth: Int,
    val id: Int?
)

class InvalidFolderException(message: String): Exception(message)

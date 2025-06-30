package com.example.locationdescriptor.location_descriptor.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.locationdescriptor.location_descriptor.domain.model.FolderDescriptor

@Entity(
    indices = [Index(value = ["name"], unique = true)]
)
data class FolderDescriptorEntity(
    val name: String,
    val parent: String,
    val depth: Int,
    @PrimaryKey val id: Int? = null
){
    fun toFolderDescriptor(): FolderDescriptor {
        return FolderDescriptor(
            name = name,
            parentName = parent,
            depth = depth,
            id = id
        )
    }
}

class InvalidFolderException(message: String): Exception(message)
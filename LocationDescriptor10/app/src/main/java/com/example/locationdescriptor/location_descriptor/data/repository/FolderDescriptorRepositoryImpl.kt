package com.example.locationdescriptor.location_descriptor.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.example.locationdescriptor.location_descriptor.data.data_source.FolderDescriptorDao
import com.example.locationdescriptor.location_descriptor.data.entity.FolderDescriptorEntity
import com.example.locationdescriptor.location_descriptor.domain.model.FolderDescriptor
import com.example.locationdescriptor.location_descriptor.domain.repository.FolderDescriptorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.jvm.Throws

class FolderDescriptorRepositoryImpl(
    private val dao: FolderDescriptorDao
): FolderDescriptorRepository {

    override fun getFolderDescriptors(): Flow<List<FolderDescriptor>> {

        val folderDescriptorEntity = dao.getFolderDescriptors()

       return folderDescriptorEntity.map { list ->
           list.map{entity ->
               entity.toFolderDescriptor()
           }
       }

    }

    override suspend fun insertFolderDescriptor(folderDescriptor: FolderDescriptor) {

        val folderDescriptorEntity = toFolderDescriptorEntity(folderDescriptor)

        dao.insertFolderDescriptor(folderDescriptorEntity)
    }

    override suspend fun deleteFolderDescriptor(folderDescriptor: FolderDescriptor) {

        val folderDescriptorEntity = toFolderDescriptorEntity(folderDescriptor)

        dao.deleteFolderDescriptor(folderDescriptorEntity)
    }

    override suspend fun deleteFolderDescriptorByName(name: String) {
        dao.deleteFolderDescriptorByName(name)
    }

    override suspend fun nameExist(name: String): Boolean {
       return dao.nameExist(name)
    }

    private fun toFolderDescriptorEntity(folderDescriptor: FolderDescriptor): FolderDescriptorEntity
    {
        return  FolderDescriptorEntity(
            name = folderDescriptor.name,
            parent = folderDescriptor.parentName,
            depth = folderDescriptor.depth
        )
    }
}
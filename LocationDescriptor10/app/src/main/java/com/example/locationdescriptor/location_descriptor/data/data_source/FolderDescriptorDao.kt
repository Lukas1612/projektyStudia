package com.example.locationdescriptor.location_descriptor.data.data_source

import androidx.room.*
import com.example.locationdescriptor.location_descriptor.data.entity.FolderDescriptorEntity
import com.example.locationdescriptor.location_descriptor.domain.model.FolderDescriptor
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDescriptorDao {

    @Query("SELECT * FROM folderdescriptorentity ORDER BY depth ASC")
    fun getFolderDescriptors(): Flow<List<FolderDescriptorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolderDescriptor(descriptor: FolderDescriptorEntity)

    @Delete
    suspend fun deleteFolderDescriptor(descriptor: FolderDescriptorEntity)

    @Query("DELETE FROM folderdescriptorentity WHERE name = :name")
    suspend fun deleteFolderDescriptorByName(name: String)

    @Query("SELECT EXISTS(SELECT * FROM folderdescriptorentity WHERE name = :name)")
    fun nameExist(name: String): Boolean
}
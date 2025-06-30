package com.example.locationdescriptor.location_descriptor.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.locationdescriptor.location_descriptor.data.entity.FolderDescriptorEntity
import com.example.locationdescriptor.location_descriptor.data.entity.PhotoDescriptionEntity

@Database(
    entities = [
        PhotoDescriptionEntity::class,
        FolderDescriptorEntity::class
               ],
    version = 1
)
abstract class DescriptionDatabase: RoomDatabase(){
    abstract val descriptionDao: DescriptionDao
    abstract val folderDescriptorDao: FolderDescriptorDao

    companion object {
        const val DATABASE_NAME = "description_db"
    }
}
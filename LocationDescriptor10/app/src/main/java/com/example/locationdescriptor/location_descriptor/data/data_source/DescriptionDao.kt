package com.example.locationdescriptor.location_descriptor.data.data_source

import kotlinx.coroutines.flow.Flow
import androidx.room.*
import com.example.locationdescriptor.location_descriptor.data.entity.PhotoDescriptionEntity
import com.example.locationdescriptor.location_descriptor.domain.model.PhotoDescription

@Dao
interface DescriptionDao {

    @Query("SELECT * FROM photodescriptionentity")
    fun getDescriptions(): Flow<List<PhotoDescriptionEntity>>

    @Query("SELECT * FROM photodescriptionentity WHERE id = :id")
    suspend fun getDescriptionById(id: Int): PhotoDescriptionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDescription(description: PhotoDescriptionEntity)

    @Delete
    suspend fun deleteDescription(description: PhotoDescriptionEntity)
}

package com.example.locationdescriptor.location_descriptor.data.repository

import com.example.locationdescriptor.location_descriptor.data.data_source.DescriptionDao
import com.example.locationdescriptor.location_descriptor.data.entity.PhotoDescriptionEntity
import com.example.locationdescriptor.location_descriptor.domain.model.PhotoDescription
import com.example.locationdescriptor.location_descriptor.domain.repository.DescriptionRepository
import kotlinx.coroutines.flow.*


class DescriptionRepositoryImpl(
    private val dao: DescriptionDao
    ): DescriptionRepository {

      override fun getPhotos(): Flow<List<PhotoDescription>> {

          val photoDescriptionsEntities = dao.getDescriptions()


         return photoDescriptionsEntities.map { list ->
             list.map { entity ->
                 entity.toPhotoDescription()
             }
         }

    }





    override suspend fun getPhotoById(id: Int): PhotoDescription? {
        return dao.getDescriptionById(id)?.toPhotoDescription()
    }

    override suspend fun insertPhoto(photo: PhotoDescription) {

        val photoEntity = toPhotoDescriptionEntity(photo)

         dao.insertDescription(photoEntity)
    }

    override suspend fun deletePhoto(photo: PhotoDescription) {

        val photoEntity = toPhotoDescriptionEntity(photo)

        dao.deleteDescription(photoEntity)
    }

    private fun toPhotoDescriptionEntity(photo: PhotoDescription): PhotoDescriptionEntity
    {
        if(photo.id != null && photo.id != -1)
        {
            return PhotoDescriptionEntity(
                photo.title,
                photo.note,
                photo.latitude,
                photo.longitude,
                photo.folderName,
                photo.id
            )
        }else
        {
            return PhotoDescriptionEntity(
                photo.title,
                photo.note,
                photo.latitude,
                photo.longitude,
                photo.folderName
            )
        }
    }

}
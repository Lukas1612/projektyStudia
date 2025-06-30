package com.example.locationdescriptor.location_descriptor.domain.use_case.description


import com.example.locationdescriptor.location_descriptor.domain.model.PhotoDescription
import com.example.locationdescriptor.location_descriptor.domain.repository.DescriptionRepository


class DeletePhotoDescription(private val repository: DescriptionRepository) {

  /*  @Throws(IOException::class)
    suspend operator fun invoke(description: PhotoDescription)
    {
        if(internalStorageFacade.deletePhoto(description.title))
        {
            repository.deletePhoto(description)
        }else
        {
            throw IOException("deleting photo from internal storage failed")
        }

    }*/

    suspend operator fun invoke(description: PhotoDescription)
    {
        repository.deletePhoto(description)
    }
}
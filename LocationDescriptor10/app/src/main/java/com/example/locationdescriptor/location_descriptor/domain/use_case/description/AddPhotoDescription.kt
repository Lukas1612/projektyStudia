package com.example.locationdescriptor.location_descriptor.domain.use_case.description


import com.example.locationdescriptor.location_descriptor.domain.model.InvalidPhotoException
import com.example.locationdescriptor.location_descriptor.domain.model.PhotoDescription
import com.example.locationdescriptor.location_descriptor.domain.repository.DescriptionRepository


class AddPhotoDescription(private val repository: DescriptionRepository) {

   /* @Throws(InvalidPhotoException::class, IOException::class)
    suspend operator fun invoke(photo: Bitmap, description: PhotoDescription)
    {
        if(description.title.isBlank())
        {
            throw InvalidPhotoException("The title of the photo can't be empty")
        }
        if(description.path.isBlank())
        {
            throw InvalidPhotoException("The path of the photo can't be empty")
        }
        if(description.latitude == null || description.longitude == null)
        {
            throw InvalidPhotoException("The coordinates can't be empty")
        }

        if(internalStorageFacade.savePhoto(photo, description.title!!))
        {
            repository.insertPhoto(description)
        }else {
            throw IOException("saving photo to internal storage failed")
        }
    }*/

    @Throws(InvalidPhotoException::class)
    suspend operator fun invoke(description: PhotoDescription)
    {
        if(description.title.isBlank())
        {
            throw InvalidPhotoException("The title of the photo can't be empty")
        }
        if(description.latitude == null || description.longitude == null)
        {
            throw InvalidPhotoException("The coordinates can't be empty")
        }

        repository.insertPhoto(description)
    }
}
package com.example.locationdescriptor.location_descriptor.domain.use_case.description

import android.graphics.Bitmap
import com.example.locationdescriptor.location_descriptor.data.entity.PhotoDescriptionEntity
import com.example.locationdescriptor.location_descriptor.domain.model.PhotoDescription
import com.example.locationdescriptor.location_descriptor.domain.repository.InternalStorageFacade
import com.example.locationdescriptor.location_descriptor.domain.repository.DescriptionRepository
import kotlinx.coroutines.flow.Flow
import java.io.IOException

class GetPhotoDescriptions(private val repository: DescriptionRepository) {

    operator fun invoke(): Flow<List<PhotoDescription>>
    {
        return repository.getPhotos()
    }

}
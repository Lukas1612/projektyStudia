package com.example.locationdescriptor.location_descriptor.domain.use_case.photo

import com.example.locationdescriptor.location_descriptor.domain.repository.InternalStorageFacade

class GetPhotoDirectoryPath(private val storage: InternalStorageFacade) {

     operator fun invoke(): String?
    {
        return storage.getPhotoDirectoryPath()
    }
}
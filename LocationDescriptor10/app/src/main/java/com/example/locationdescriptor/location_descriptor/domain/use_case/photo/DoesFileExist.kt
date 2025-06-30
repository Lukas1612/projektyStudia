package com.example.locationdescriptor.location_descriptor.domain.use_case.photo

import com.example.locationdescriptor.location_descriptor.domain.repository.InternalStorageFacade

class DoesFileExist(private val storage: InternalStorageFacade) {

    operator fun invoke(fileName: String): Boolean
    {
        return storage.doesFileExist(fileName)
    }
}
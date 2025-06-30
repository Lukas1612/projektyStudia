package com.example.locationdescriptor.location_descriptor.domain.use_case.photo

import android.graphics.Bitmap
import com.example.locationdescriptor.location_descriptor.domain.repository.InternalStorageFacade

class DeletePhoto(private val storage: InternalStorageFacade) {

    operator fun invoke(fileName: String): Boolean
    {
        return storage.deletePhoto(fileName)
    }
}
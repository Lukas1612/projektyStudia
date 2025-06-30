package com.example.locationdescriptor.location_descriptor.domain.use_case.photo

import android.graphics.Bitmap
import com.example.locationdescriptor.location_descriptor.domain.repository.InternalStorageFacade
import java.io.IOException
import kotlin.jvm.Throws

class GetPhoto(private val storage: InternalStorageFacade) {


    suspend operator fun invoke(fileName: String): Bitmap?
    {
        return storage.getPhotoByName(fileName)
    }
}
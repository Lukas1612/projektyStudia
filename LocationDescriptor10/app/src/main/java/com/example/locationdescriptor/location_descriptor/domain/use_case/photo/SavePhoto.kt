package com.example.locationdescriptor.location_descriptor.domain.use_case.photo

import android.graphics.Bitmap
import com.example.locationdescriptor.location_descriptor.domain.repository.InternalStorageFacade
import java.io.IOException
import kotlin.jvm.Throws

class SavePhoto(private val storage: InternalStorageFacade) {

    @Throws(IOException::class)
    operator fun invoke(photo: Bitmap, fileName: String): Boolean
    {
        return storage.savePhoto(photo, fileName)
    }
}
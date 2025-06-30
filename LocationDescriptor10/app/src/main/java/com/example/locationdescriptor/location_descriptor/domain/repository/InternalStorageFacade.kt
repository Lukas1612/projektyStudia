package com.example.locationdescriptor.location_descriptor.domain.repository

import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import java.io.IOException
import kotlin.jvm.Throws

interface InternalStorageFacade {


     suspend fun getPhotoByName(filename: String): Bitmap?

     @Throws(IOException::class)
     fun savePhoto(content: Bitmap, filename: String): Boolean
     fun deletePhoto(filename: String): Boolean
     fun getPhotoDirectoryPath(): String
     fun doesFileExist(filename: String): Boolean
     fun savePdfToFile(fileName: String, myPdfDocument: PdfDocument)
}
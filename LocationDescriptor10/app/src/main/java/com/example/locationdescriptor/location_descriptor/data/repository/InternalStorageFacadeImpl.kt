package com.example.locationdescriptor.location_descriptor.data.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.example.locationdescriptor.location_descriptor.domain.repository.InternalStorageFacade
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import kotlin.jvm.Throws

class InternalStorageFacadeImpl(private val context: Context): InternalStorageFacade {

    override suspend fun getPhotoByName(filename: String): Bitmap? {
        return withContext(Dispatchers.IO) {

            try {
                println(" filename: $filename.jpg ")
                val inputStream: FileInputStream = context.openFileInput("$filename.jpg")
                return@withContext BitmapFactory.decodeStream(inputStream)
            }catch (e: IOException)
            {
                println(" --> $e ")
                return@withContext null
            }
        }
    }

    @Throws(IOException::class)
    override fun savePhoto(content: Bitmap, filename: String): Boolean {

        return try {
            context.openFileOutput("$filename.jpg", MODE_PRIVATE).use { stream ->
                if(!content.compress(Bitmap.CompressFormat.JPEG, 95, stream)){
                    throw IOException("couldn't save bitmap")
                }
            }
            true
        }catch (e: IOException)
        {
            e.printStackTrace()

            false
        }
    }

    override fun deletePhoto(filename: String): Boolean {
        return try{
            context.deleteFile("$filename.jpg")
            true
        }catch (e: IOException)
        {
            e.printStackTrace()
            false
        }
    }

    override fun getPhotoDirectoryPath(): String {
        return context.filesDir.canonicalPath
    }

    override fun doesFileExist(filename: String): Boolean {
        return File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "$filename.pdf"
        ).exists()
    }

    override fun savePdfToFile(fileName: String, myPdfDocument: PdfDocument){
        var file: File = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "$fileName.pdf"
        )

        try {
            myPdfDocument.writeTo(FileOutputStream(file))

        } catch (e: IOException) {
        }
    }
}
package com.example.locationdescriptor.location_descriptor.presentation.photos.pdf_create

import android.content.Context
import android.graphics.pdf.PdfDocument
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.locationdescriptor.location_descriptor.domain.model.PhotoDescription
import com.example.locationdescriptor.location_descriptor.domain.use_case.photo.PhotoUseCases
import com.example.locationdescriptor.location_descriptor.presentation.WorkerKeys
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs

@HiltWorker
class CreatePdfWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val photoUseCases: PhotoUseCases
): CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result{

        withContext(Dispatchers.IO)  {
            val inputJsonArray = inputData.getString(WorkerKeys.KEY_MY_JSONARRAY)
            val fileName = inputData.getString(WorkerKeys.KEY_MY_FILE_NAME)

            val selectedPhotoDescriptions: List<PhotoDescription> = getListOfPhotoDescriptionFromJsonArray(inputJsonArray)

            val pdfDocument = createPdf(selectedPhotoDescriptions)

            savePdfToFile(fileName!!, pdfDocument)
        }

        return Result.success()
    }

    private suspend fun createPdf(selectedPhotos: List<PhotoDescription>): PdfDocument
    {
        val pdfCreateHelper = PdfCreateHelper()

        selectedPhotos.forEach{description ->

            val photo = photoUseCases.getPhoto(description.title)
            val note = "note: " + description.note
            val degreesMinutesSecondsCoordinates = coordinatesDDtoDMS(
                description.latitude.toFloat(),
                description.longitude.toFloat()
            )

            val pageData = PageData(
                note = note,
                coordinates = degreesMinutesSecondsCoordinates,
                photo = photo!!
            )
            pdfCreateHelper.drawNextPage(pageData)
        }

        return pdfCreateHelper.getPdfDocument()
    }


    //Decimal Degrees to Degrees Minutes Seconds
    private fun coordinatesDDtoDMS(latitude: Float, longitude: Float): String
    {
        var latSeconds = latitude*3600
        val latDegrees = (latSeconds/3600)
        latSeconds = abs(latSeconds%3600)
        val latMinutes = latSeconds/60
        latSeconds %= 60

        var longSeconds  = longitude*3600
        val longDegrees = longSeconds /3600
        longSeconds  = abs(longSeconds %3600)
        val longMinutes  = longSeconds /60
        longSeconds %= 60

        val lat = if(latitude>=0){
            "N"
        }else
        {
            "S"
        }

        val lon = if(longitude>=0){
            "E"
        }else
        {
            "W"
        }

        return ""+ abs(latDegrees).toInt() +"°" + latMinutes.toInt()+"'"+ "%.1f".format(latSeconds) + "\"$lat " + abs(longDegrees).toInt() + "°"+ longMinutes.toInt() + "'"+ "%.1f".format(longSeconds) +"\"$lon"
    }

    private fun getListOfPhotoDescriptionFromJsonArray(jsonArray: String?): List<PhotoDescription>{
        return Gson().fromJson<List<PhotoDescription>>(jsonArray, object : TypeToken<List<PhotoDescription>>() {}.type)
    }

    private fun savePdfToFile(fileName: String,  myPdfDocument: PdfDocument){
        photoUseCases.savePdfToFile(fileName, myPdfDocument)
    }


}
package com.example.locationdescriptor.location_descriptor.domain.use_case.photo

import android.graphics.pdf.PdfDocument
import com.example.locationdescriptor.location_descriptor.domain.repository.InternalStorageFacade

class SavePdfToFile(private val storage: InternalStorageFacade) {
    operator fun invoke(fileName: String, myPdfDocument: PdfDocument)
    {
         storage.savePdfToFile(fileName, myPdfDocument)
    }
}
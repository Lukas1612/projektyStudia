package com.example.locationdescriptor.location_descriptor.presentation.photos.pdf_create

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.Typeface
import android.graphics.text.LineBreaker
import android.text.*
import androidx.core.graphics.withTranslation

class PdfCreateHelper {

    private var myPdfDocument: PdfDocument = PdfDocument()
    private var titlePaint: Paint = Paint()
    private var notePaint: TextPaint
    private var photoPaint: Paint

    private val pageWidth = 792f
    private val photoHeight = 300f
    private val spaceBetweenElements = 20f
    private val coordinatesTextHeight = 15

    private var pagesNumber = 0

    init {
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))
        titlePaint.textSize = 15F
        titlePaint.textAlign = Paint.Align.CENTER

        notePaint = TextPaint()
        photoPaint = Paint()
    }


    fun drawNextPage(pageData: PageData){
        val scaledPhoto = makeScaledPhoto(pageData.photo)

        val noteTextLayout = getStaticLayout(pageData.note, notePaint, 300)

        val pageHeight = calculatePageHeight(scaledPhoto.height, noteTextLayout.height)

        val myPageInfo =
            PdfDocument.PageInfo.Builder(pageWidth.toInt(), pageHeight.toInt(), pagesNumber + 1).create()
        val page = myPdfDocument.startPage(myPageInfo)
        val canvas = page.canvas

        drawPhoto(canvas, scaledPhoto)
        drawCoordinates(canvas, pageData.coordinates, scaledPhoto.height)
        drawNote(canvas, noteTextLayout,scaledPhoto.height)

        myPdfDocument.finishPage(page)
    }

    private fun drawPhoto(canvas: Canvas, scaledPhoto: Bitmap)
    {
        val x: Float = (pageWidth / 2) - (scaledPhoto.width/2)
        val y: Float = 0f
        canvas.drawBitmap(scaledPhoto, x, y, photoPaint)
    }

    private fun drawCoordinates(canvas: Canvas, coordinates: String, scaledPhotoHeight: Int)
    {
        val x: Float = pageWidth / 2
        val y: Float = scaledPhotoHeight + spaceBetweenElements
        canvas.drawText(coordinates, x, y, titlePaint)
    }

    private fun drawNote(canvas: Canvas, noteTextLayout: StaticLayout, scaledPhotoHeight: Int)
    {
        val x: Float = (pageWidth / 2) - (noteTextLayout.width/2)
        val y = scaledPhotoHeight + spaceBetweenElements + coordinatesTextHeight + spaceBetweenElements
        noteTextLayout.draw(canvas, x, y)
    }



    private fun makeScaledPhoto(photo: Bitmap): Bitmap
    {
        val photoProportions: Float = (photo.width).toFloat() / (photo.height).toFloat()
        val photoWidth = photoProportions * photoHeight

        return Bitmap.createScaledBitmap(photo, photoWidth.toInt(), photoHeight.toInt(), false)
    }

    private fun calculatePageHeight(photoHeight: Int, noteHeight: Int): Float
    {
        return photoHeight + spaceBetweenElements + coordinatesTextHeight + spaceBetweenElements + noteHeight + spaceBetweenElements
    }

    private fun getStaticLayout(
        text: CharSequence,
        textPaint: TextPaint,
        width: Int,
        start: Int = 0,
        end: Int = text.length,
        alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
        textDir: TextDirectionHeuristic = TextDirectionHeuristics.LTR,
        spacingMult: Float = 1f,
        spacingAdd: Float = 0f,
    ): StaticLayout {
        return StaticLayout.Builder
            .obtain(text, start, end, textPaint, width)
            .setAlignment(alignment)
            .setTextDirection(textDir)
            .setLineSpacing(spacingAdd, spacingMult)
            .setBreakStrategy(LineBreaker.BREAK_STRATEGY_SIMPLE)
            .setJustificationMode(LineBreaker.JUSTIFICATION_MODE_NONE)
            .build()
    }

    private fun StaticLayout.draw(canvas: Canvas, x: Float, y: Float) {
        canvas.withTranslation(x, y) {
            draw(this)
        }
    }

    fun getPdfDocument(): PdfDocument{
        return myPdfDocument
    }


}
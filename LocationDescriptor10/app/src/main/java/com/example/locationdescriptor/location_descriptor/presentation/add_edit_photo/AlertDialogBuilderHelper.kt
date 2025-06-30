package com.example.locationdescriptor.location_descriptor.presentation.add_edit_photo

import android.app.AlertDialog
import android.content.Context

class AlertDialogBuilderHelper(val context: Context) {
    private lateinit var doYouWantToSaveWindowBuilder: AlertDialog.Builder
    private lateinit var doYouWantToCancelWindowBuilder: AlertDialog.Builder

    init{
        doYouWantToSaveWindowBuilder = AlertDialog.Builder(context)
        doYouWantToCancelWindowBuilder = AlertDialog.Builder(context)
    }

    fun createDoYouWantToSaveAlert(onOkClickListener: () -> Unit)
    {
        doYouWantToSaveWindowBuilder.setMessage("do you want to save the photo?")
            .setCancelable(false)
            .setNegativeButton("cancel"){ dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("ok"){ dialog, which ->
                onOkClickListener()
                dialog.dismiss()
            }.show()
    }

    fun createDoYouWantToCancelAlert(onOkClickListener: () -> Unit)
    {
        doYouWantToCancelWindowBuilder.setMessage("do you want to cancel without saving the photo?")
            .setCancelable(false)
            .setNegativeButton("cancel"){ dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("ok"){ dialog, which ->
                onOkClickListener()
                dialog.dismiss()
            }.show()
    }
}
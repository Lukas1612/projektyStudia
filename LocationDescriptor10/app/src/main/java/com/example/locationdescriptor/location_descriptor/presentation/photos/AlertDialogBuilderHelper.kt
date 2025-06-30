package com.example.locationdescriptor.location_descriptor.presentation.photos

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText


class AlertDialogBuilderHelper(val context: Context) {

    private lateinit var folderNameExistWindowBuilder: AlertDialog.Builder
    private lateinit var fileNameExistWindowBuilder: AlertDialog.Builder
    private lateinit var nonDataSelectedtWindowBuilder: AlertDialog.Builder
    private lateinit var newFolderWindowBuilder: AlertDialog.Builder
    private lateinit var deleteAlertDialogBuilder: AlertDialog.Builder
    private lateinit var PdfFileWindowBuilder: AlertDialog.Builder

    init{
        folderNameExistWindowBuilder = AlertDialog.Builder(context)
        fileNameExistWindowBuilder = AlertDialog.Builder(context)
        newFolderWindowBuilder = AlertDialog.Builder(context)
        deleteAlertDialogBuilder = AlertDialog.Builder(context)
        PdfFileWindowBuilder = AlertDialog.Builder(context)
        nonDataSelectedtWindowBuilder = AlertDialog.Builder(context)
    }

    fun createFolderNameAlreadyExistsAlert()
    {
        folderNameExistWindowBuilder.setMessage("this folder name is already used")
            .setCancelable(true)
            .setPositiveButton("ok"){dialog, which ->
                dialog.dismiss()
            }.show()
    }

    fun createFileNameAlreadyExistsAlert()
    {
        fileNameExistWindowBuilder.setMessage("this file name is already used")
            .setCancelable(true)
            .setPositiveButton("ok"){dialog, which ->
                dialog.dismiss()
            }.show()

    }



    fun createNewFolderCreationWindow(onOkClickListener: (folderName: String) -> Unit)
    {
        val editText = EditText(context)

        newFolderWindowBuilder
            .setTitle("create a new folder")
            .setView(editText)
            .setCancelable(false)
            .setPositiveButton("ok"){dialog, which ->

                val folderName: String = editText.text.toString()
                onOkClickListener(folderName)

                dialog.dismiss()
            }.show()

    }

    fun createDeleteItemsAskingWarningWindow(onOkClickListener: () -> Unit)
    {
        deleteAlertDialogBuilder.setMessage("are you sure you want to delete the selected items?")
            .setTitle("deleting item")
            .setCancelable(false)
            .setNegativeButton("cancel"){dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("ok"){dialog, which ->
                onOkClickListener()
                dialog.dismiss()
            }.show()
    }

    fun createPdfConversionWindow(onOkClickListener: (fileName: String) -> Unit)
    {
        val editText = EditText(context)

        PdfFileWindowBuilder.setMessage("choose file name")
            .setView(editText)
            .setCancelable(false)
            .setPositiveButton("ok"){dialog, which ->

                val fileName: String = editText.text.toString()
                onOkClickListener(fileName)
                dialog.dismiss()
            }.show()


    }
    fun createNonDataSelectedAlertWindow()
    {
        nonDataSelectedtWindowBuilder.setMessage("non photo selected")
            .setCancelable(true)
            .setPositiveButton("ok"){dialog, which ->
                dialog.dismiss()
            }.show()
    }

}
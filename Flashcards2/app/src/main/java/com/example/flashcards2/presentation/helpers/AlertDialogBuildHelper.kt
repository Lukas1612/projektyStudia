package com.example.flashcards2.presentation.helpers

import android.app.AlertDialog
import android.content.Context

class AlertDialogBuildHelper {

    fun doYouWantToRemoveSelectedElementsAlert(context: Context, onYesClicked: () -> Unit){

        // Create the object of AlertDialog Builder class
        val builder = AlertDialog.Builder(context)

        // Set the message show for the Alert time
        builder.setMessage("Do you want to remove the selected element/elements?")

        // Set Alert Title
        builder.setTitle("Alert !")

        // Set Cancelable false for when the user clicks
        // on the outside the Dialog Box then it will remain show
        builder.setCancelable(false)

        // Set the positive button with yes name Lambda
        // OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes") {

            // When the user click yes button then app will close
                dialog, which -> onYesClicked()
        }

        // Set the Negative button with No name Lambda
        // OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No") {

            // If user click no then dialog box is canceled.
                dialog, which -> dialog.cancel()
        }

        // Create the Alert dialog
        val alertDialog = builder.create()

        // Show the Alert Dialog box
        alertDialog.show()
    }
}
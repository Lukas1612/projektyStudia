package com.example.locationdescriptor.location_descriptor.presentation.add_edit_photo

import android.graphics.Bitmap

sealed class AddEditDescriptionEvent{
    data class EnteredTitle(val value: String): AddEditDescriptionEvent()
    data class EnteredNote(val value: String?): AddEditDescriptionEvent()
    data class EnteredLongitude(val value: Double?): AddEditDescriptionEvent()
    data class EnteredLatitude(val value: Double?): AddEditDescriptionEvent()
    data class TakenPhoto(val value: Bitmap): AddEditDescriptionEvent()
    object GetCurrentLocation: AddEditDescriptionEvent()
    object SaveButtonClicked: AddEditDescriptionEvent()
    object CancelButtonClicked: AddEditDescriptionEvent()
    object SaveConfirmed: AddEditDescriptionEvent()
    object CancelConfirmed: AddEditDescriptionEvent()
}

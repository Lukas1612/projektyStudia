package com.example.readyreadingkotlin.FullPassageMenu.ui.flashcard

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Flashcard_groups(  var id: Int?,  var user_id: Int?,  var title: String?) : Parcelable  {

    override fun toString(): String {
        return "Flashcard_groups(id=$id, user_id=$user_id, title=$title)"
    }
}
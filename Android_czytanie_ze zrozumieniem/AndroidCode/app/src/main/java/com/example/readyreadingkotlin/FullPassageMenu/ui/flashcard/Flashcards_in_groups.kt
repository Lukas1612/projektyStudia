package com.example.readyreadingkotlin.FullPassageMenu.ui.flashcard

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Flashcards_in_groups( var group: Flashcard_groups?, var flashcards: ArrayList<Flashcard>? ): Parcelable {
    override fun toString(): String {
        return "Flashcards_in_groups(group=$group, flashcards=$flashcards)"
    }
}


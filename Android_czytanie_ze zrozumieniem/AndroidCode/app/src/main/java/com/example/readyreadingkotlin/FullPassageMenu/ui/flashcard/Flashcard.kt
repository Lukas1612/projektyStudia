package com.example.readyreadingkotlin.FullPassageMenu.ui.flashcard

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Flashcard(var id: Int?,
                var user_id: Int?,
                var word: String?,
                var translation: String?,
                var example_sentence: String?,
                var translated_sentence: String?,
                var group_id: Int?): Parcelable {

    override fun toString(): String {
        return "Flashcard(id=$id, user_id=$user_id, word=$word, translation=$translation, example_sentence=$example_sentence, translated_sentence=$translated_sentence, group_id=$group_id)"
    }
}
package com.example.readyreadingkotlin.questions

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class QuestionnaireQuestions(var id:Int?,
                             var text: String?
                             ): Parcelable {
    override fun toString(): String {
        return "QuestionnaireQuestions(id=$id, text=$text)"
    }
}
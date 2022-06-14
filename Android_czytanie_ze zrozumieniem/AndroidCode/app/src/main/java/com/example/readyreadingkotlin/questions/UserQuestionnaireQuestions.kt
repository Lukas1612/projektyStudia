package com.example.readyreadingkotlin.questions

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class UserQuestionnaireQuestions( var id: Int?,  var questionnaire_questions_id: Int?, var user_id: Int?, var passage_id: Int?): Parcelable {
    override fun toString(): String {
        return "UserQuestionnaireQuestions(id=$id, questionnaire_questions_id=$questionnaire_questions_id, user_id=$user_id, passage_id=$passage_id)"
    }
}
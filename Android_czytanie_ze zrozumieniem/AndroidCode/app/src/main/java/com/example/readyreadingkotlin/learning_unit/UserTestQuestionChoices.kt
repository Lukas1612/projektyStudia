package com.example.readyreadingkotlin.learning_unit
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class UserTestQuestionChoices( var id: Int?,  var test_question_id: Int?,  var user_id: Int?,  var question_choice_id: Int? ): Parcelable {

    override fun toString(): String {
        return "UserTestQuestionChoices(id=$id, test_question_id=$test_question_id, user_id=$user_id, question_choice_id=$question_choice_id)"
    }
}
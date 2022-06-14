package com.example.readyreadingkotlin.learning_unit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class TestQuestionsWithChoices(val id: Int?, val text: String?,  val test_passage_id: Int?,  val questionChoices: List<TestQuestionsChoices>?, var userQuestionChoices: List<UserTestQuestionChoices>?) : Parcelable {

    override fun toString(): String {
        return "TestQuestionsWithChoices(id=$id, text=$text, test_passage_id=$test_passage_id, questionChoices=$questionChoices, userQuestionChoices=$userQuestionChoices)"
    }
}
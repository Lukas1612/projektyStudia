package com.example.readyreadingkotlin.learning_unit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class TestQuestionsChoices(val  id: Int?, val text: String?, val correct_or_false: String?, val  test_questions_id: Int?) : Parcelable {

    override fun toString(): String {
        return "TestQuestionsChoices(id=$id, text=$text, correct_or_false=$correct_or_false, test_questions_id=$test_questions_id)"
    }
}
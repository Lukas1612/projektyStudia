package com.example.readyreadingkotlin.learning_unit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class UserTests(var id: Int?, var score: String?, var test_passage_id: Int?,  var user_id: Int?, var date: String?) : Parcelable {

    override fun toString(): String {
        return "UserTests(id=$id, score=$score, test_passage_id=$test_passage_id, user_id=$user_id, date=$date)"
    }
}
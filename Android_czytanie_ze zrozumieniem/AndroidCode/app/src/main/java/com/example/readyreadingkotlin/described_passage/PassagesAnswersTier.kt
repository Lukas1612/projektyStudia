package com.example.readyreadingkotlin.described_passage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class PassagesAnswersTier( var id: Int?, var tier: Int?,  var passage_id: Int?,  var question_id: Int?, var user_id: Int?): Parcelable {
    override fun toString(): String {
        return "PassagesAnswersTier(id=$id, tier=$tier, passage_id=$passage_id, question_id=$question_id, user_id=$user_id)"
    }
}
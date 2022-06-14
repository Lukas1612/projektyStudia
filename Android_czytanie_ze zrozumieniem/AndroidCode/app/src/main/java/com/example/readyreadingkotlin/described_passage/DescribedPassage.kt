package com.example.readyreadingkotlin.described_passage


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class DescribedPassage(val id: Int,
                       val text: String? = null,
                       val title: String? = null,
                       val tips: List<Tips>? = null,
                       val questions: List<Questions>? = null,
                       val translationtext: String?,
                       val test_passage_id: Int?,
                       val answersTier: List<PassagesAnswersTier>?) : Parcelable
{
    override fun toString(): String {
        return "DescribedPassage(id=$id, text=$text, title=$title, tips=$tips, questions=$questions, translationtext=$translationtext, test_passage_id=$test_passage_id, answersTier=$answersTier)"
    }
}

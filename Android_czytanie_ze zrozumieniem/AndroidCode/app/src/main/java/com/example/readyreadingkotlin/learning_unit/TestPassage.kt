package com.example.readyreadingkotlin.learning_unit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class TestPassage(val id: Int?, val text: String?, val  title: String?) : Parcelable {

    override fun toString(): String {
        return "TestPassage(id=$id, text=$text, title=$title)"
    }
}

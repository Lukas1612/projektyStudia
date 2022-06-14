package com.example.readyreadingkotlin.described_passage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Questions(var id: Int, var question: String?, var answer: String?,  var passage_id: Int) : Parcelable
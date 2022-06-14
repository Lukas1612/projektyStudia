package com.example.readyreadingkotlin.described_passage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tips(var id: Int, var text: String?, var passage_id: Int) : Parcelable
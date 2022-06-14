package com.example.readyreadingkotlin


import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Passage(var id: Int, var title: String?, var text: String?) : Parcelable
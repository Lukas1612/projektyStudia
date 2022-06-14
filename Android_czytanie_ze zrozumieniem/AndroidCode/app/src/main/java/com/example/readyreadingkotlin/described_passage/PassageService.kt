package com.example.readyreadingkotlin.described_passage

import com.example.readyreadingkotlin.described_passage.DescribedPassage
import retrofit2.Call
import retrofit2.http.GET

interface PassageService  {

    @GET("describedPassage/language=polish")
    fun getPassages(): Call<List<DescribedPassage>>

}
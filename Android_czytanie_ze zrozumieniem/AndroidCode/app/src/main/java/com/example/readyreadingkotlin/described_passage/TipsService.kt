package com.example.readyreadingkotlin.described_passage

import com.example.readyreadingkotlin.described_passage.Tips
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TipsService {

    @GET("tips/id={id}")
    fun getTips(@Path("id")  id: Int): Call<List<Tips>>
}
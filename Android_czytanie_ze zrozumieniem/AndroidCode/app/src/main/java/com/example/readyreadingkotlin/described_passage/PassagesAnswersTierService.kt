package com.example.readyreadingkotlin.described_passage

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PassagesAnswersTierService {

    @POST("Passages_answers_tier")
    fun savePassagesAnswersTier(@Body body: PassagesAnswersTierBodyPost): Call<Long>
}
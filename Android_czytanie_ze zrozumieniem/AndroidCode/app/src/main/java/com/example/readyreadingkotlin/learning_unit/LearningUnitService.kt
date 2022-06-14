package com.example.readyreadingkotlin.learning_unit

import com.example.readyreadingkotlin.described_passage.DescribedPassage
import retrofit2.Call
import retrofit2.http.GET

interface LearningUnitService {
    @GET("LearningUnits/language=polish")
    fun getLearningUnits(): Call<List<LearningUnit>>
}
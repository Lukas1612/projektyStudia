package com.example.readyreadingkotlin.questions

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface UserQuestionnaireQuestionsService {
    @POST("user_questionnaire_questions")
     fun postUserQuestionnaireQuestionsService(@Body body: UserQuestionnaireQuestionsBodyPost): Call<Long>

    @GET("user_questionnaire_questions")
    fun getUserQuestionnaireQuestionsService(@Body body: UserQuestionnaireQuestionsBodyPost): Call<List<UserQuestionnaireQuestions>>
}
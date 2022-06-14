package com.example.readyreadingkotlin.questions

import com.example.readyreadingkotlin.learning_unit.LearningUnit
import retrofit2.Call
import retrofit2.http.GET


interface QuestionnaireQuestionsService {
    @GET("questionnaire_questions")
    fun getQuestionnaireQuestions(): Call<List<QuestionnaireQuestions>>
}
package com.example.readyreadingkotlin.learning_unit

import com.example.readyreadingkotlin.FullPassageMenu.ui.flashcard.FlashcardBodyPost
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserTestQuestionChoicesService {

    @POST("user_test_question_choices")
    fun postUserTestQuestionChoices(@Body body: UserTestQuestionChoicesBodyPost): Call<Long>
}
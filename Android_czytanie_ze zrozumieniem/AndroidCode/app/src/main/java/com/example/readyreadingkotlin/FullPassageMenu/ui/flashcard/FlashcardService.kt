package com.example.readyreadingkotlin.FullPassageMenu.ui.flashcard

import com.example.readyreadingkotlin.learning_unit.UserTestsBodyPost
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FlashcardService {

    @POST("flashcard")
    fun postFlashcard(@Body body: FlashcardBodyPost): Call<Long>
}
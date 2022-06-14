package com.example.readyreadingkotlin.auth

import com.example.readyreadingkotlin.FullPassageMenu.ui.flashcard.Flashcard
import com.example.readyreadingkotlin.FullPassageMenu.ui.flashcard.Flashcards_in_groups
import com.example.readyreadingkotlin.learning_unit.LearningUnit
import com.example.readyreadingkotlin.learning_unit.UserTests
import com.example.readyreadingkotlin.questions.UserQuestionnaireQuestions
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST(Constants.LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<String>

    @GET(Constants.GET_FLASHCARDS)
    fun getFlashcards(@Header("Authorization") token: String): Call<List<Flashcard>>
    @GET(Constants.GET_FLASHCARDS_IN_GROUPS)
    fun getFlashcardsInGroups(@Header("Authorization") token: String): Call<List<Flashcards_in_groups>>

    @DELETE(Constants.DELETE_USER_TEST)
    fun deleteUserTest(@Header("Authorization", ) token: String, @Path("id") id: Int): Call<Boolean>

    @GET(Constants.GET_USER_TEST)
    fun getUserTest(@Header("Authorization") token: String): Call<List<UserTests>>

    @DELETE(Constants.DELETE_USER_FLASHCARD)
    fun deleteUserFlashcard(@Header("Authorization") token: String, @Path("id") id: Int): Call<Boolean>

    @GET(Constants.GET_USER_QUESTIONNAIRE_QUESTIONS)
    fun getUserQuestionnaireQuestions(@Header("Authorization") token: String):  Call<List<UserQuestionnaireQuestions>>

    @GET(Constants.GET_USER_LEARNING_UNITS)
    fun getLearningUnits(@Header("Authorization") token: String, @Path("name") name: String):  Call<List<LearningUnit>>


}
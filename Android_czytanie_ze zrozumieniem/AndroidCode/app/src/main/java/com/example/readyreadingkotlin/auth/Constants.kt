package com.example.readyreadingkotlin.auth

import retrofit2.http.DELETE

object Constants {

    // Endpoints
    const val BASE_URL = "http://10.0.2.2:8080/app/"
    const val LOGIN_URL = "authenticate"
    const val GET_FLASHCARDS = "flashcard"
    const val DELETE_USER_TEST = "user_tests/id={id}"
    const val GET_USER_TEST = "user_tests"
    const val GET_FLASHCARDS_IN_GROUPS ="flashcards_in_groups"
    const val DELETE_USER_FLASHCARD = "flashcard/id={id}"
    const val GET_USER_QUESTIONNAIRE_QUESTIONS = "user_questionnaire_questions"
    const val GET_USER_LEARNING_UNITS = "LearningUnits/language={name}"
}
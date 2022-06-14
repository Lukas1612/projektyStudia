package com.example.readyreadingkotlin.learning_unit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface UserTestsService {
    @POST("user_tests")
    fun postUserTest(@Body body: UserTestsBodyPost): Call<Long>

    @DELETE("user_tests")
    fun delete(@Body body: UserTestsBodyPost): Call<Boolean>
}